package com.example.coupon.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * Swagger/OpenAPI 3 설정 클래스
 * API 문서 자동 생성을 위한 설정을 정의합니다.
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 개발 서버"),
                        new Server().url("https://api.example.com").description("운영 서버")
                ));
    }

    /**
     * API 정보 설정
     */
    private Info apiInfo() {
        return new Info()
                .title("쿠폰 발급 시스템 API")
                .description("RabbitMQ를 활용한 쿠폰 발급 시스템의 REST API 문서입니다.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("개발팀")
                        .email("dev@example.com")
                        .url("https://github.com/example/coupon-system"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
    @EventListener(ApplicationReadyEvent.class)
    public void showSwaggerUrl() {
        log.info("=================================");
        log.info("Swagger UI: http://localhost:8080/swagger-ui.html");
        log.info("API Docs: http://localhost:8080/v3/api-docs");
        log.info("=================================");
    }
}