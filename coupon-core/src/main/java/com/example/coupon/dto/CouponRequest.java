package com.example.coupon.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "쿠폰 발급 요청")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CouponRequest implements Serializable {

    @Schema(description = "사용자 ID", required = true, example = "user123")
    private String userId;

    @Schema(description = "쿠폰 유형", required = true, example = "DISCOUNT_AMOUNT",
            allowableValues = {"DISCOUNT_AMOUNT", "DISCOUNT_RATE"})
    private String couponType;

    @Schema(description = "할인 금액 (금액권인 경우)", example = "5000")
    private Integer discountAmount;

    @Schema(description = "할인율 (%) (할인율 쿠폰인 경우)", example = "10")
    private Integer discountRate;

    @Schema(description = "만료일 (발급일로부터 n일)", example = "30")
    private Long expiryDays;
}
