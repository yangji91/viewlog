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

    private BufferedReader reader;
    private Session session;

    public ViewLogThread(InputStream in, Session session) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.session = session;

    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                session.sendText(line + "<br>");
            }
        } catch (IOException e) {
            log.error("读取日志出错：", e);
        }
    }
}
