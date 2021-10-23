package com.codeisrun.viewlog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 配置线程池
 *
 * @author liubinqiang
 * @date 2021-10-21
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {
    @Value("${threadPool.corePoolSize}")
    private int corePoolSize;
    @Value("${threadPool.maximumPoolSize}")
    private int maximumPoolSize;
    @Value("${threadPool.keepAliveTime}")
    private long keepAliveTime;

    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {
        //阻塞队列
        BlockingQueue blockingQueue = new ArrayBlockingQueue<Runnable>(512);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                blockingQueue,
                threadFactory,
                rejectedExecutionHandler);
        log.info("创建线程池完成：corePoolSize={},maximumPoolSize={}", corePoolSize, maximumPoolSize);
        return threadPoolExecutor;
    }
}
