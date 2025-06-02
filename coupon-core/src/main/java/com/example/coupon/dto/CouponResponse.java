package com.example.coupon.dto;

import com.example.coupon.entity.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 쿠폰 발급 응답 DTO
 * 쿠폰 발급 결과를 클라이언트에 전달하기 위한 데이터 객체
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse implements Serializable {
    
    private Long id;
    private String code;
    private String userId;
    private String couponType;
    private Integer discountAmount;
    private Integer discountRate;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private CouponStatus status;
    private String message;  // 처리 메시지
    private boolean success;  // 성공 여부
}
