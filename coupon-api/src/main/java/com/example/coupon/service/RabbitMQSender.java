package com.example.coupon.service;

import com.example.coupon.config.RabbitMQConfig;
import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * RabbitMQ 메시지 발신 서비스
 * RabbitMQ를 통해 메시지를 보내고 응답을 수신합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQResponseHandler responseHandler;

    /**
     * 쿠폰 발급 요청을 RabbitMQ를 통해 전송하고 응답을 비동기적으로 처리합니다.
     * 
     * @param request 쿠폰 발급 요청
     * @return 응답을 담은 CompletableFuture
     */
    public CompletableFuture<CouponResponse> sendCouponRequest(CouponRequest request) {
        // 요청 ID 생성
        String correlationId = UUID.randomUUID().toString();
        
        // 응답을 위한 CompletableFuture 생성
        CompletableFuture<CouponResponse> future = new CompletableFuture<>();
        
        try {
            log.info("=== RabbitMQ로 쿠폰 발급 요청 전송 시작 - 사용자: {}, 상관관계 ID: {}", request.getUserId(), correlationId);
            
            // 응답 핸들러 등록 (전송 전에 먼저 등록)
            responseHandler.registerPendingRequest(correlationId, future);
            
            // 메시지를 RabbitMQ로 전송
            // Producer : 메시지를 생성하고 RabbitMQ 로 전송
            // 직접 큐로 보내는게 아니라 Exchange 로 전송함.
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_COUPON,
                    RabbitMQConfig.ROUTING_KEY_REQUEST,
                    request,
                    message -> {
                        // 상관관계 ID 설정
                        message.getMessageProperties().setCorrelationId(correlationId);
                        // 응답을 받을 큐 이름 설정
                        message.getMessageProperties().setReplyTo(RabbitMQConfig.QUEUE_COUPON_RESPONSE);
                        
                        log.info("메시지 헤더 설정 완료 - CorrelationId: {}, ReplyTo: {}", 
                                correlationId, RabbitMQConfig.QUEUE_COUPON_RESPONSE);
                        return message;
                    });
            
            log.info("RabbitMQ 메시지 전송 완료 - CorrelationId: {}", correlationId);
            return future;
            
        } catch (Exception e) {
            log.error("쿠폰 발급 요청 전송 중 오류 발생 - CorrelationId: {}", correlationId, e);
            future.completeExceptionally(e);
            return future;
        }
    }
}
