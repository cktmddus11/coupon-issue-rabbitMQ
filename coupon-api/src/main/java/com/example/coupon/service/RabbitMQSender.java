package com.example.coupon.service;

import com.example.coupon.config.RabbitMQConfig;
import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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
    
    // 요청 ID와 응답을 연결하기 위한 맵
    private final Map<String, CompletableFuture<CouponResponse>> pendingRequests = new ConcurrentHashMap<>();

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
        
        // 요청 ID와 Future를 맵에 저장
        pendingRequests.put(correlationId, future);
        
        try {
            log.info("RabbitMQ로 쿠폰 발급 요청 전송 - 사용자: {}, 상관관계 ID: {}", request.getUserId(), correlationId);
            
            // 메시지를 RabbitMQ로 전송
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_COUPON,
                    RabbitMQConfig.ROUTING_KEY_REQUEST,
                    request,
                    message -> {
                        // 상관관계 ID 설정
                        message.getMessageProperties().setCorrelationId(correlationId);
                        // 응답을 받을 큐 이름 설정
                        message.getMessageProperties().setReplyTo(RabbitMQConfig.QUEUE_COUPON_RESPONSE);
                        return message;
                    });
            
            // 응답 핸들러 등록
            responseHandler.registerPendingRequest(correlationId, future);
            
            log.debug("쿠폰 발급 요청 전송 완료, 응답 대기 중...");
            return future;
            
        } catch (Exception e) {
            // 오류 발생 시 요청 맵에서 제거하고 예외 전파
            pendingRequests.remove(correlationId);
            log.error("쿠폰 발급 요청 전송 중 오류 발생", e);
            future.completeExceptionally(e);
            return future;
        }
    }
}
