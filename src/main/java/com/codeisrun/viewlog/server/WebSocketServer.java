package com.codeisrun.viewlog.server;

import com.codeisrun.viewlog.common.CmdEnum;
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
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadPoolExecutor;

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
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    private Process process = null;
    private InputStream inputStream = null;
    private InputStreamReader inputStreamReader = null;
    private BufferedReader bufferedReader = null;
    private long start;

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers,
                       @RequestParam String ip,
                       @RequestParam String cmd,
                       @RequestParam String path,
                       @RequestParam String key,
                       @RequestParam String length) {
        log.info("---onOpen---收到连接：{},userAgent={},X-Real-IP={},ip={},cmd={},path={}",
                getChannelInfo(session), headers.get(ConstStr.userAgent), headers.get(ConstStr.xRealIp), ip, cmd, path);
        sendMsg(session, "------websocket连接成功，正在读取日志中......");
        start = System.currentTimeMillis();
        if (!viewLogService.verifyPath(ip, path)) {
            sendMsg(session, "文件路径不支持");
        }
        cmd = CmdEnum.getCmd(cmd, path, key, length);
        try {
            inputStream = doCmd(ip, systemConfig.getServerUsername(ip), systemConfig.getServerPassword(ip), cmd, path);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            ViewLogTask viewLogTask = new ViewLogTask(bufferedReader, session, CmdEnum.isRealTimeCmd(cmd));
            threadPoolExecutor.execute(viewLogTask);
        } catch (Exception e) {
            log.error("查看日志报错：", e);
            sendMsg(session, e.getMessage());
        }
    }

    private InputStream doCmd(String serverIp, String user, String pwd, String cmd, String path) throws Exception {
        if (!LinuxUtil.verifyCmd(cmd)) {
            log.error("不支持该命令：ip={},cmd={}", serverIp, cmd);
            throw new Exception("不支持该命令");
        }
        if (serverIp != null) {
            //执行远程命令
            //sshpass -p root ssh root@192.168.0.8 "ll"
            cmd = String.format("sshpass -p %s ssh -o StrictHostKeyChecking=no %s@%s \"%s\"", pwd, user, serverIp, cmd);
        }
        //log.info("执行命令：{}", cmd);
        String[] commands = {"/bin/sh", "-c", cmd};

        try {
            process = Runtime.getRuntime().exec(commands);
            //log.info("process.waitFor={}", process.waitFor());
            //在执行实时日志的时候不能打印
            //String errorMsg = new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().collect(Collectors.joining("\n"));
            //log.info("执行命令是否有错误信息：{}", errorMsg);
            inputStream = process.getInputStream();
            return inputStream;
        } catch (Exception e) {
            log.error("执行命令报错：", e);
            throw e;
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
    public void onClose(Session session) {
        log.info("关闭连接收到请求：{}", session);
        session.sendText("------websocket已关闭，耗时：" + (System.currentTimeMillis() - start) + "毫秒------<br>");
        try {
            if (inputStreamReader != null) {
                log.info("websocket释放资源-1-inputStreamReader.hashCode={}", inputStreamReader.hashCode());
                inputStreamReader.close();
            }
            if (bufferedReader != null) {
                log.info("websocket释放资源-2-bufferedReader.hashCode={}", bufferedReader.hashCode());
                bufferedReader.close();
            }
            if (inputStream != null) {
                log.info("websocket释放资源-3-inputStream.hashCode={}", inputStream.hashCode());
                inputStream.close();
            }
            if (process != null) {
                log.info("websocket释放资源-4-process.hashCode={}", process.hashCode());
                process.destroy();
            }
            log.info("websocket释放资源-5-process.hashCode={}", session.hashCode());
            session.close();
        } catch (Exception exception) {
            log.error("websocket释放资源报错：", exception);
        }
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
