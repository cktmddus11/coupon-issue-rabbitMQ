package com.example.coupon.entity;

/**
 * 쿠폰 상태 열거형
 * 쿠폰의 상태를 나타내는 열거형 클래스
 */
public enum CouponStatus {
    ISSUED,     // 발급됨
    USED,       // 사용됨
    EXPIRED,    // 만료됨
    CANCELLED   // 취소됨
}
