package com.codeisrun.viewlog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liubinqiang
 */
@Component
public class SystemConfig {
    @Value("${http-path}")
    public String httpPath;
    @Value("${netty-websocket.port}")
    public String wsPort;
    @Value("${netty-websocket.path}")
    public String wsPath;
    @Value("${log.path}")
    public String logPaths;
    @Value("${log.scope}")
    public Integer logScope;
    @Value("${log.link}")
    public String logLink;
    @Value("${log.menu}")
    public String logMenu;
    @Value("${log.servers}")
    public String logServers;

    public String getServerUsername(String ip) {
        if (ip != null && logServers != null && logServers.contains(ip)) {
            String[] servers = logServers.split(",");
            for (String s : servers) {
                if (s.contains(ip)) {
                    String[] items = s.split("\\|");
                    return items[1];
                }
            }
        }
        return null;
    }

    public String getServerPassword(String ip) {
        if (ip != null && logServers != null && logServers.contains(ip)) {
            String[] servers = logServers.split(",");
            for (String s : servers) {
                if (s.contains(ip)) {
                    String[] items = s.split("\\|");
                    return items[2];
                }
            }
        }
        return null;
    }
}
