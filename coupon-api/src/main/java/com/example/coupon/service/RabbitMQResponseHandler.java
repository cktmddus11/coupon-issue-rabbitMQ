package com.example.coupon.service;

import com.example.coupon.config.RabbitMQConfig;
import com.example.coupon.dto.CouponResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ 응답 핸들러
 * RabbitMQ로부터 응답을 수신하고 처리합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQResponseHandler {

    private final MessageConverter messageConverter;
    
    // 요청 ID와 CompletableFuture를 연결하는 맵
    private final Map<String, CompletableFuture<CouponResponse>> pendingRequests = new ConcurrentHashMap<>();
    
    // 요청 시간을 저장하는 맵
    private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();
    
    // 요청 타임아웃을 관리하는 스케줄러
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // 요청 타임아웃 시간 (밀리초)
    private static final long REQUEST_TIMEOUT_MS = 10000;
    
    /**
     * 초기화 메서드
     * 스케줄러를 시작하여 타임아웃된 요청을 정리합니다.
     */
    @PostConstruct
    public void init() {
        // 주기적으로 타임아웃된 요청 확인
        scheduler.scheduleAtFixedRate(this::cleanupTimedOutRequests, 
                1000, 1000, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 타임아웃된 요청을 정리합니다.
     */
    private void cleanupTimedOutRequests() {
        long now = System.currentTimeMillis();
        
        // 타임아웃된 요청을 찾아서 제거
        requestTimestamps.entrySet().removeIf(entry -> {
            String correlationId = entry.getKey();
            long requestTime = entry.getValue();
            boolean isTimedOut = (now - requestTime) > REQUEST_TIMEOUT_MS;
            
            if (isTimedOut) {
                CompletableFuture<CouponResponse> future = pendingRequests.remove(correlationId);
                if (future != null && !future.isDone()) {
                    log.warn("요청 타임아웃: {}", correlationId);
                    future.completeExceptionally(
                            new RuntimeException("요청 처리 시간이 초과되었습니다."));
                }
            }
            
            return isTimedOut;
        });
    }
    
    /**
     * 대기 중인 요청을 등록합니다.
     * 
     * @param correlationId 상관관계 ID
     * @param future 응답을 받을 CompletableFuture
     */
    public void registerPendingRequest(String correlationId, 
                                      CompletableFuture<CouponResponse> future) {
        pendingRequests.put(correlationId, future);
        requestTimestamps.put(correlationId, System.currentTimeMillis());
        log.debug("요청 등록 - CorrelationId: {}", correlationId);
    }
    
    /**
     * RabbitMQ 응답 리스너
     * 쿠폰 응답 큐에서 메시지를 수신합니다.
     * 
     * @param message 수신된 메시지
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_COUPON_RESPONSE)
    public void handleCouponResponse(Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        
        log.info("응답 수신 - CorrelationId: {}", correlationId);
        
        if (correlationId == null) {
            log.warn("상관관계 ID가 없는 응답을 수신했습니다.");
            return;
        }
        
        // 해당 요청 ID에 대한 CompletableFuture 조회
        CompletableFuture<CouponResponse> future = pendingRequests.remove(correlationId);
        requestTimestamps.remove(correlationId);
        
        if (future == null) {
            log.warn("대기 중인 요청이 없는 응답을 수신했습니다: {}", correlationId);
            return;
        }
        
        try {
            // 메시지 변환
            CouponResponse response = (CouponResponse) messageConverter.fromMessage(message);
            log.info("쿠폰 발급 응답 처리 완료 - 상관관계 ID: {}, 성공: {}", 
                    correlationId, response.isSuccess());
            
            // CompletableFuture 완료 처리
            future.complete(response);
            
        } catch (Exception e) {
            log.error("응답 처리 중 오류 발생 - CorrelationId: {}", correlationId, e);
            future.completeExceptionally(e);
        }
    }
}
