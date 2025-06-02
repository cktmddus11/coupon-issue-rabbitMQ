package com.example.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 쿠폰 발급 요청 DTO
 * 클라이언트에서 쿠폰 발급을 요청할 때 사용하는 데이터 객체
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequest implements Serializable {
    
    private String userId;  // 사용자 ID
    private String couponType;  // 쿠폰 유형
    private Integer discountAmount;  // 할인 금액 (금액권)
    private Integer discountRate;  // 할인율 (%) (할인율 쿠폰)
    private Long expiryDays;  // 만료일 (발급일로부터 n일)
}
