package com.example.coupon.service;

import com.example.coupon.config.RabbitMQConfig;
import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 쿠폰 요청 리스너
 * RabbitMQ로부터 쿠폰 발급 요청을 수신하고 처리합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRequestListener {

    private final CouponIssueService couponIssueService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 쿠폰 발급 요청 리스너
     * 쿠폰 요청 큐에서 메시지를 수신하고 처리합니다.
     * 
     * @param request 쿠폰 발급 요청
     * @param correlationId 상관관계 ID
     * @param replyTo 응답 큐 이름
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_COUPON_REQUEST)
    public void handleCouponRequest(CouponRequest request, 
                                   String correlationId,
                                   String replyTo) {
        log.info("쿠폰 발급 요청 수신 - 사용자: {}, 상관관계 ID: {}", 
                request.getUserId(), correlationId);
        
        try {
            // 쿠폰 발급 처리
            CouponResponse response = couponIssueService.issueCoupon(request);
            
            // 응답 전송
            sendResponse(response, correlationId, replyTo);
            
        } catch (Exception e) {
            log.error("쿠폰 발급 처리 중 오류 발생", e);
            
            // 오류 응답 생성
            CouponResponse errorResponse = CouponResponse.builder()
                    .userId(request.getUserId())
                    .couponType(request.getCouponType())
                    .success(false)
                    .message("쿠폰 발급 처리 중 오류가 발생했습니다: " + e.getMessage())
                    .build();
            
            // 오류 응답 전송
            sendResponse(errorResponse, correlationId, replyTo);
        }
    }
    
    /**
     * 응답을 RabbitMQ를 통해 전송합니다.
     * 
     * @param response 응답 객체
     * @param correlationId 상관관계 ID
     * @param replyTo 응답 큐 이름
     */
    private void sendResponse(CouponResponse response, 
                             String correlationId, 
                             String replyTo) {
        try {
            log.info("쿠폰 발급 응답 전송 - 상관관계 ID: {}, 성공: {}", 
                    correlationId, response.isSuccess());
            
            // 응답 큐로 메시지 전송
            rabbitTemplate.convertAndSend(
                    "", // 기본 교환기 사용
                    replyTo, // 응답 큐 이름
                    response, // 응답 객체
                    message -> {
                        // 상관관계 ID 설정
                        message.getMessageProperties().setCorrelationId(correlationId);
                        return message;
                    });
            
        } catch (Exception e) {
            log.error("응답 전송 중 오류 발생", e);
        }
    }
}
