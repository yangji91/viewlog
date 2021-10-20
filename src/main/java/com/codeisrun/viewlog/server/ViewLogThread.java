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
public class ViewLogThread extends Thread {

    private BufferedReader bufferedReader;
    private Session session;

    public ViewLogThread(BufferedReader bufferedReader, Session session) {
        this.bufferedReader = bufferedReader;
        this.session = session;
    }

    @Override
    public void run() {
        log.info("当前查看日志线程：ViewLogThread={}-{}", Thread.currentThread().getId(), Thread.currentThread().getName());
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                session.sendText(line + "<br>");
            }
        } catch (IOException e) {
            session.sendText("查看日志工具报错：" + e.toString());
            log.error("读取日志出错：", e);
        }
    }
}
