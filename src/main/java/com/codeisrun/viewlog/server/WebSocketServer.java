package com.codeisrun.viewlog.server;

import com.codeisrun.viewlog.common.ConstStr;
import com.codeisrun.viewlog.service.IViewLogService;
import com.codeisrun.viewlog.util.LogUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author liubinqiang
 */
@ServerEndpoint(prefix = "netty-websocket")
@Component
public class WebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    @Autowired
    private IViewLogService viewLogService;


    private Process process;
    private InputStream inputStream;

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) {
        LOGGER.info("---onOpen---收到连接：{},userAgent={},ip={}", getChannelInfo(session), headers.get(ConstStr.userAgent), headers.get(ConstStr.xRealIp));
        sendMsg(session, "websocket连接成功");
        try {
            // 执行tail -f命令
            String cmd = parameterMap.getParameter("cmd");
            cmd = LogUtil.urlDecoder(cmd);
            LOGGER.info("执行命令请求：{}", cmd);
            if (viewLogService.verifyCmd(cmd)) {
                process = Runtime.getRuntime().exec(cmd);
                inputStream = process.getInputStream();
                // 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
                ViewLogThread thread = new ViewLogThread(inputStream, session);
                thread.start();
            } else {
                sendMsg(session, "命令不合法：" + cmd);
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
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
    public void onClose(Session session) throws IOException {
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (process != null)
            process.destroy();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
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
