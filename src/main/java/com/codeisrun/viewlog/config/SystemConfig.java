package com.codeisrun.viewlog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liubinqiang
 */
@Component
public class SystemConfig {
    @Value("${netty-websocket.port}")
    public String wsPort;
    @Value("${netty-websocket.path}")
    public String wsPath;
    @Value("${log.search.viewLines}")
    public String logSearchViewLines;

    public String getServerUsername(String ip) {

        return null;
    }

    public String getServerPassword(String ip) {

        return null;
    }
}
