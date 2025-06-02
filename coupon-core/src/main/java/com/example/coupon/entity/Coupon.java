package com.example.coupon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 쿠폰 엔티티
 * 쿠폰의 정보를 저장하는 엔티티 클래스
 */
@Entity
@Table(name = "coupons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String code;  // 쿠폰 코드
    
    @Column(nullable = false)
    private String userId;  // 사용자 ID
    
    @Column(nullable = false)
    private String couponType;  // 쿠폰 유형 (할인, 금액권 등)
    
    private Integer discountAmount;  // 할인 금액
    
    private Integer discountRate;  // 할인율 (%)
    
    @Column(nullable = false)
    private LocalDateTime issuedAt;  // 발급 시간
    
    private LocalDateTime expiresAt;  // 만료 시간
    
    private LocalDateTime usedAt;  // 사용 시간
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;  // 쿠폰 상태
    
    // 쿠폰 발급 전 초기화 메서드
    public void initialize() {
        this.issuedAt = LocalDateTime.now();
        this.status = CouponStatus.ISSUED;
    }
}
