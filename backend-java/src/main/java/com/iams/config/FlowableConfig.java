package com.iams.config;

import lombok.Data;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/13 16:14
 * @Desc:
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class FlowableConfig {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    @Bean
    public ProcessEngine processEngine() {
        return new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(url)
                .setJdbcDriver(driverClassName)
                .setJdbcUsername(username)
                .setJdbcPassword(password)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setAsyncExecutorActivate(true)
                .buildProcessEngine();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }
}
