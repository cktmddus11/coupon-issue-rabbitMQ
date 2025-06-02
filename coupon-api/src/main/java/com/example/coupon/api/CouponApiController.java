package com.example.coupon.api;

import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.CouponStatus;
import com.example.coupon.repository.CouponRepository;
import com.example.coupon.service.RabbitMQSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 쿠폰 API 컨트롤러
 * REST API를 통해 쿠폰 관련 요청을 처리합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponApiController {

    private final RabbitMQSender rabbitMQSender;
    private final CouponRepository couponRepository;
    
    /**
     * 쿠폰 발급 API
     * 
     * @param request 쿠폰 발급 요청 정보
     * @return 발급 결과
     */
    @PostMapping("/issue")
    public ResponseEntity<CouponResponse> issueCoupon(@RequestBody CouponRequest request) {
        log.info("쿠폰 발급 API 호출 - 사용자: {}", request.getUserId());
        
        try {
            // RabbitMQ를 통해 쿠폰 발급 요청 전송
            CompletableFuture<CouponResponse> future = rabbitMQSender.sendCouponRequest(request);
            
            // 최대 5초 동안 응답 대기
            CouponResponse response = future.get(5, TimeUnit.SECONDS);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("쿠폰 발급 API 에러", e);
            CouponResponse errorResponse = CouponResponse.builder()
                    .success(false)
                    .message("쿠폰 발급 처리 중 오류가 발생했습니다: " + e.getMessage())
                    .build();
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 사용자 쿠폰 목록 조회 API
     * 
     * @param userId 사용자 ID
     * @return 쿠폰 목록
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Coupon>> getUserCoupons(@PathVariable String userId) {
        log.info("사용자 쿠폰 목록 조회 - 사용자: {}", userId);
        List<Coupon> coupons = couponRepository.findByUserId(userId);
        return ResponseEntity.ok(coupons);
    }
    
    /**
     * 사용자 유효 쿠폰 목록 조회 API
     * 
     * @param userId 사용자 ID
     * @return 유효한 쿠폰 목록
     */
    @GetMapping("/user/{userId}/valid")
    public ResponseEntity<List<Coupon>> getValidUserCoupons(@PathVariable String userId) {
        log.info("사용자 유효 쿠폰 목록 조회 - 사용자: {}", userId);
        List<Coupon> coupons = couponRepository.findByUserIdAndStatus(userId, CouponStatus.ISSUED);
        return ResponseEntity.ok(coupons);
    }
}
