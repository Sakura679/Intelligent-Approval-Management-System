package com.iams;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/4 15:58
 * @Desc:
 */
@Slf4j
@SpringBootApplication
public class IamsApplication {
    public static void main(String[] args) {
        SpringApplication.run(IamsApplication.class, args);

        log.info("Knife4j 增强界面（推荐）：http://localhost:8081/doc.html");
        log.info("原生 Swagger UI：http://localhost:8081/swagger-ui.html");
        log.info("API 文档 JSON：http://localhost:8081/v2/api-docs");
    }
}
