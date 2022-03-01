package com.flyhub.ideaMS.configurations;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * This Class initializes the Thread pool manager. It creates a pool of threads
 * that will execute update statements that change the transaction states.
 *
 * @since 25/08/2021
 * @author Benjamin E Ndugga
 */

@Configuration
public class TaskThreadConfig {
    
    private static final Logger log = Logger.getLogger(TaskThreadConfig.class.getName());

    @Value("${thread.corepoolsize}")
    private int corepoolsize;
    @Value("${thread.corepoolsize}")
    private int maxpoolsize;
    @Value("${thread.corepoolsize}")
    private int maxqueuecapacity;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corepoolsize);
        executor.setMaxPoolSize(maxpoolsize);
        executor.setQueueCapacity(maxqueuecapacity);
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor executor1) -> {
            log.info("one of the threads have been rejected...");
        });
        executor.setThreadNamePrefix("txn_db_task_exec_thread");
        executor.initialize();

        log.debug("set-core-pool-size: " + executor.getCorePoolSize());
        log.debug("set-max-pool-size: " + executor.getMaxPoolSize());

        return executor;
    }
}
