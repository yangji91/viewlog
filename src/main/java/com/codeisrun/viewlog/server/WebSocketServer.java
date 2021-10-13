package com.codeisrun.viewlog.server;

import com.codeisrun.viewlog.common.ConstStr;
import com.codeisrun.viewlog.util.LinuxUtil;
import com.codeisrun.viewlog.util.LogUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) {
        LOGGER.info("---onOpen---收到连接：{},userAgent={},ip={}", getChannelInfo(session), headers.get(ConstStr.userAgent), headers.get(ConstStr.xRealIp));
        sendMsg(session, "websocket连接成功");
        String cmd = parameterMap.getParameter("cmd");
        cmd = LogUtil.urlDecoder(cmd);
        InputStream inputStream = LinuxUtil.doCmd(cmd, null);
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
    public void onClose(Session session) throws IOException {
        //TODO 关闭资源
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
