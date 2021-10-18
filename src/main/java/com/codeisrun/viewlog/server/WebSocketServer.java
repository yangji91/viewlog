package com.codeisrun.viewlog.server;

import com.codeisrun.viewlog.common.ConstStr;
import com.codeisrun.viewlog.config.SystemConfig;
import com.codeisrun.viewlog.service.IViewLogService;
import com.codeisrun.viewlog.util.LinuxUtil;
import com.codeisrun.viewlog.util.LogUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.InputStream;
import java.util.Map;

/**
 * @author liubinqiang
 */
@Slf4j
@ServerEndpoint(path = "${netty-websocket.path}", port = "${netty-websocket.port}")
@Component
public class WebSocketServer {


    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private IViewLogService viewLogService;

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String ip, @RequestParam String cmd, @RequestParam String path) {
        log.info("---onOpen---收到连接：{},userAgent={},X-Real-IP={},ip={},cmd={},path={}",
                getChannelInfo(session), headers.get(ConstStr.userAgent), headers.get(ConstStr.xRealIp), ip, cmd, path);
        sendMsg(session, "websocket连接成功");
        if (!viewLogService.verifyPath(ip, path)) {
            sendMsg(session, "文件路径不支持");
        }
        cmd = LogUtil.urlDecoder(cmd);
        InputStream inputStream = null;
        try {
            inputStream = LinuxUtil.doCmd(ip, systemConfig.getServerUsername(ip), systemConfig.getServerPassword(ip), cmd, path);
        } catch (Exception e) {
            log.error("查看日志报错：{}", e);
            sendMsg(session, e.getMessage());
        }
        ViewLogThread thread = new ViewLogThread(inputStream, session);
        thread.start();
    }

    private void sendMsg(Session session, String msg) {
        session.sendText(msg + "<br/>");
    }


    public String getChannelInfo(Session session) {
        if (session != null && session.channel() != null) {
            return String.format("channelId=%s,localAddress=%s,remoteAddress=%s",
                    session.channel().id().asShortText(),
                    session.localAddress(),
                    session.remoteAddress());
        }
        return null;
    }

    @OnClose
    public void onClose(Session session) {
        //TODO 关闭资源
        log.info("关闭连接：{}", session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("连接报错：{}", session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("收到消息：{}", message);
        session.sendText("Hello Netty!");
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }


}
