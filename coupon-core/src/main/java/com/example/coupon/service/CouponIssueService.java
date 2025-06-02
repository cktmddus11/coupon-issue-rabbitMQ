package com.example.coupon.service;

import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.CouponStatus;
import com.example.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 쿠폰 발급 서비스
 * 쿠폰 발급 관련 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponRepository couponRepository;

    /**
     * 쿠폰을 발급합니다.
     * 
     * @param request 쿠폰 발급 요청 정보
     * @return 발급된 쿠폰 정보
     */
    @Transactional
    public CouponResponse issueCoupon(CouponRequest request) {
        log.info("쿠폰 발급 시작 - 사용자: {}, 쿠폰 유형: {}", request.getUserId(), request.getCouponType());
        
        try {
            // 쿠폰 엔티티 생성
            Coupon coupon = Coupon.builder()
                    .userId(request.getUserId())
                    .code(generateCouponCode())
                    .couponType(request.getCouponType())
                    .discountAmount(request.getDiscountAmount())
                    .discountRate(request.getDiscountRate())
                    .status(CouponStatus.ISSUED)
                    .issuedAt(LocalDateTime.now())
                    .build();
            
            // 만료일 설정
            if (request.getExpiryDays() != null) {
                coupon.setExpiresAt(LocalDateTime.now().plusDays(request.getExpiryDays()));
            }
            
            // 쿠폰 저장
            Coupon savedCoupon = couponRepository.save(coupon);
            log.info("쿠폰 발급 완료 - 코드: {}", savedCoupon.getCode());
            
            // 응답 생성
            return CouponResponse.builder()
                    .id(savedCoupon.getId())
                    .code(savedCoupon.getCode())
                    .userId(savedCoupon.getUserId())
                    .couponType(savedCoupon.getCouponType())
                    .discountAmount(savedCoupon.getDiscountAmount())
                    .discountRate(savedCoupon.getDiscountRate())
                    .issuedAt(savedCoupon.getIssuedAt())
                    .expiresAt(savedCoupon.getExpiresAt())
                    .status(savedCoupon.getStatus())
                    .success(true)
                    .message("쿠폰이 성공적으로 발급되었습니다.")
                    .build();
            
        } catch (Exception e) {
            log.error("쿠폰 발급 실패", e);
            return CouponResponse.builder()
                    .userId(request.getUserId())
                    .couponType(request.getCouponType())
                    .success(false)
                    .message("쿠폰 발급에 실패했습니다: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * 고유한 쿠폰 코드를 생성합니다.
     * 
     * @return 생성된 쿠폰 코드
     */
    private String generateCouponCode() {
        // UUID를 사용하여 고유한 코드 생성
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
