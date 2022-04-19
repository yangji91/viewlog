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
        int count = 0;
        int emptyCount = 0;
        int logCount = 0;
        try {
            while (true) {
                count++;
                line = bufferedReader.readLine();
                if (line != null) {
                    session.sendText(line + "<br>");
                    logCount++;
                } else {
                    emptyCount++;
                }
                //如果不是实时日志，并且读取多次依然没有数据，关闭连接
                if (!isRealTimeLog && emptyCount > 10) {
                    session.sendText("------日志已读取完，累计读取" + logCount + "行日志，websocket将要关闭------<br>");
                    //暂停下线程，让把消息传给浏览器，再关闭连接
                    threadSleep();
                    session.close();
                    break;
                }
            }
            log.info("本次查看日志完成：共尝试读取{}次，其中读取到日志{}行，空数据{}行", count, logCount, emptyCount);
        } catch (IOException e) {
            session.sendText("------查看日志工具报错：" + e.toString());
            log.error("读取日志出错：", e);
        }
    }

    private void threadSleep() {
        try {
            //让线程暂停下，把上面的消息发送到浏览器端
            Thread.sleep(10);
        } catch (InterruptedException e) {
            log.error("线程暂停失败：", e);
        }
    }
}
