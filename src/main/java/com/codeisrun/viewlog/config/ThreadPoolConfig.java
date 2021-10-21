package com.codeisrun.viewlog.config;

import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置线程池
 *
 * @author liubinqiang
 * @date 2021-10-21
 */
@Configuration
public class ThreadPoolConfig {
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return null;
    }
}
