package com.sre.analysis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyuan
 * @date 2020/8/6 17:39
 */
@Configuration
@Slf4j
public class ThreadPoolConfig {


    @Bean
    public ExecutorService getThreadPool() {
        log.info("ExecutorService getThreadPool()...");
        return new TraceThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>());
    }

}
