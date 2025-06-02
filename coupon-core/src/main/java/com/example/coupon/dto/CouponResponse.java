package com.example.coupon.dto;

import com.example.coupon.entity.CouponStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CouponResponse implements Serializable {
    @Schema(description = "쿠폰 ID", example = "1")
    private Long id;

    @Schema(description = "쿠폰 코드", example = "ABC123XYZ")
    private String code;

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "쿠폰 유형", example = "DISCOUNT_AMOUNT")
    private String couponType;

    @Schema(description = "할인 금액", example = "5000")
    private Integer discountAmount;

    @Schema(description = "할인율", example = "10")
    private Integer discountRate;

    @Schema(description = "발급 시간", example = "2023-01-01T12:00:00")
    private LocalDateTime issuedAt;

    @Schema(description = "만료 시간", example = "2023-01-31T12:00:00")
    private LocalDateTime expiresAt;

    @Schema(description = "쿠폰 상태", example = "ISSUED")
    private CouponStatus status;

    @Schema(description = "처리 메시지", example = "쿠폰이 성공적으로 발급되었습니다.")
    private String message;

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
