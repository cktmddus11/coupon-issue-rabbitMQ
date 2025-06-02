package com.example.coupon.service;

import com.example.coupon.config.RabbitMQConfig;
import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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
    // Consumer 에 해당
    @RabbitListener(queues = RabbitMQConfig.QUEUE_COUPON_REQUEST)
    public void handleCouponRequest(@Payload CouponRequest request,
                                    @Header(value = "amqp_correlationId", required = false) String correlationId,
                                    @Header(value = "amqp_replyTo", required = false) String replyTo) {
        log.info("=== 쿠폰 발급 요청 수신 시작 ===");
        log.info("사용자: {}, CorrelationId: {}, ReplyTo: {}", request.getUserId(), correlationId, replyTo);

        try {
            // 쿠폰 발급 처리
            log.info("쿠폰 발급 서비스 호출 시작 - 사용자: {}", request.getUserId());
            CouponResponse response = couponIssueService.issueCoupon(request);
            log.info("쿠폰 발급 서비스 완료 - 사용자: {}, 성공: {}", request.getUserId(), response.isSuccess());

            // 응답 전송 (헤더가 있는 경우만)
            if (correlationId != null && replyTo != null) {
                log.info("응답 전송 시도 - CorrelationId: {}, ReplyTo: {}", correlationId, replyTo);
                sendResponse(response, correlationId, replyTo);
            } else {
                log.warn("응답 헤더 누락 - CorrelationId: {}, ReplyTo: {}", correlationId, replyTo);
                log.info("쿠폰 발급 완료 (응답 불필요) - 사용자: {}, 성공: {}", 
                        request.getUserId(), response.isSuccess());
            }

        } catch (Exception e) {
            log.error("쿠폰 발급 처리 중 오류 발생 - 사용자: {}", request.getUserId(), e);

            if (correlationId != null && replyTo != null) {
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
        
        log.info("=== 쿠폰 발급 요청 처리 완료 ===");
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
            log.info("쿠폰 발급 응답 전송 시도 - 상관관계 ID: {}, 응답큐: {}, 성공: {}", 
                    correlationId, replyTo, response.isSuccess());
            
            if (replyTo == null || replyTo.isEmpty()) {
                log.warn("응답 큐가 지정되지 않았습니다. 응답을 보내지 않습니다.");
                return;
            }
            
            // 응답 큐로 메시지 전송
            rabbitTemplate.convertAndSend(
                    replyTo, // 직접 큐 이름 사용
                    response, // 응답 객체
                    message -> {
                        // 상관관계 ID 설정
                        if (correlationId != null) {
                            message.getMessageProperties().setCorrelationId(correlationId);
                        }
                        log.info("응답 메시지 전송 완료 - CorrelationId: {}", correlationId);
                        return message;
                    });
            
        } catch (Exception e) {
            log.error("응답 전송 중 오류 발생 - CorrelationId: {}, Error: {}", correlationId, e.getMessage(), e);
        }
    }
}
