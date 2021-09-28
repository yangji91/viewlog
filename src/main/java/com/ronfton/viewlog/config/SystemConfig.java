package com.ronfton.viewlog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liubinqiang
 */
@Component
public class SystemConfig {
    @Value("${log-path}")
    public String logPaths;
    @Value("${netty-websocket.port}")
    public String wsPort;
    @Value("${netty-websocket.path}")
    public String wsPath;
    @Value("${log-scope}")
    public int logScope;
    @Value("${log-link}")
    public String logLink;
    @Value("${http-path}")
    public String httpPath;

}
