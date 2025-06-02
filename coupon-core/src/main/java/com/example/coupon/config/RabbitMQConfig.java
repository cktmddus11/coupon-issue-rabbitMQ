package com.example.coupon.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 설정 클래스
 * RabbitMQ 관련 설정을 정의합니다.
 */
@Configuration
public class RabbitMQConfig {

    // 쿠폰 발급 요청을 처리할 큐 이름
    public static final String QUEUE_COUPON_REQUEST = "coupon.request.queue";
    
    // 쿠폰 발급 응답을 처리할 큐 이름
    public static final String QUEUE_COUPON_RESPONSE = "coupon.response.queue";
    
    // 쿠폰 발급 요청 교환기 이름
    public static final String EXCHANGE_COUPON = "coupon.exchange";
    
    // 쿠폰 발급 요청 라우팅 키
    public static final String ROUTING_KEY_REQUEST = "coupon.request";
    
    // 쿠폰 발급 응답 라우팅 키
    public static final String ROUTING_KEY_RESPONSE = "coupon.response";

    /**
     * 쿠폰 요청 큐 Bean 생성
     */
    @Bean
    public Queue couponRequestQueue() {
        return new Queue(QUEUE_COUPON_REQUEST, true);
    }

    /**
     * 쿠폰 응답 큐 Bean 생성
     */
    @Bean
    public Queue couponResponseQueue() {
        return new Queue(QUEUE_COUPON_RESPONSE, true);
    }

    /**
     * 쿠폰 관련 교환기 Bean 생성
     */
    @Bean
    public DirectExchange couponExchange() {
        return new DirectExchange(EXCHANGE_COUPON);
    }

    /**
     * 쿠폰 요청 큐와 교환기 바인딩
     */
    @Bean
    public Binding couponRequestBinding() {
        return BindingBuilder
                .bind(couponRequestQueue())
                .to(couponExchange())
                .with(ROUTING_KEY_REQUEST);
    }

    /**
     * 쿠폰 응답 큐와 교환기 바인딩
     */
    @Bean
    public Binding couponResponseBinding() {
        return BindingBuilder
                .bind(couponResponseQueue())
                .to(couponExchange())
                .with(ROUTING_KEY_RESPONSE);
    }

    /**
     * JSON 메시지 변환기 Bean 생성
     * RabbitMQ에서 메시지를 JSON 형식으로 직렬화/역직렬화 하기 위함
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate Bean 생성
     * RabbitMQ와 메시지를 주고받기 위한 템플릿
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
