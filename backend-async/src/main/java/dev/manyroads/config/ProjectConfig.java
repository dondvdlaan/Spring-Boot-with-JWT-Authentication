package dev.manyroads.config;

import dev.manyroads.controllers.AuthController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class ProjectConfig {

    Logger logger = LogManager.getLogger(ProjectConfig.class);
    private static int threadPoolSize = 6;
    private static int taskQueueSize = 3;

    /*
    Scheduler to connect the reactive CRUD operations with the none-reactive MySQL DB
     */
    @Bean
    public Scheduler jdbcScheduler() {

        logger.info("Creates a jdbcScheduler with thread pool size = {}", threadPoolSize);

        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "jdbc-pool");
    }
}
