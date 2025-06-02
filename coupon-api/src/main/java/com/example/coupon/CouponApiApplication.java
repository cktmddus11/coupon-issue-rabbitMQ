package com.example.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * 쿠폰 API 애플리케이션 메인 클래스
 * 코어 모듈의 설정을 명시적으로 가져옵니다.
 */
@SpringBootApplication
@Import(CoreConfiguration.class) // 코어 모듈 설정 명시적으로 가져오기
public class CouponApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponApiApplication.class, args);
    }
}
