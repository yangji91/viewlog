package com.codeisrun.viewlog.server;


import lombok.extern.slf4j.Slf4j;
import org.yeauty.pojo.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author liubinqiang
 */
@Slf4j
public class ViewLogTask implements Runnable {

    private BufferedReader bufferedReader;
    private Session session;
    private boolean isRealTimeLog;

    public ViewLogTask(BufferedReader bufferedReader, Session session, boolean isRealTimeLog) {
        this.bufferedReader = bufferedReader;
        this.session = session;
        this.isRealTimeLog = isRealTimeLog;
    }

    @Override
    public void run() {
        log.info("当前查看日志线程：ViewLogThread={}-{}", Thread.currentThread().getId(), Thread.currentThread().getName());
        String line;
        try {
            while (true) {
                line = bufferedReader.readLine();
                if (line != null) {
                    session.sendText(line + "<br>");
                } else {
                    //如果不是实时日志，关闭连接
                    if (!isRealTimeLog) {
                        session.sendText("------日志已读取完，websocket将要关闭------<br>");
                        session.close();
                        return;
                    }
                }
            }
        } catch (IOException e) {
            session.sendText("查看日志工具报错：" + e.toString());
            log.error("读取日志出错：", e);
        }
    }
}
