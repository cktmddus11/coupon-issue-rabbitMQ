package com.example.coupon;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 코어 모듈 설정 클래스
 * 코어 모듈의 컴포넌트, 엔티티, 레포지토리에 대한 스캔 설정
 */
@Configuration
@ComponentScan(basePackages = {
    "com.example.coupon.service", 
    "com.example.coupon.config"
})
@EntityScan(basePackages = "com.example.coupon.entity")
@EnableJpaRepositories(basePackages = "com.example.coupon.repository")
public class CoreConfiguration {
    // 추가 설정이 필요한 경우 여기에 구현
}
