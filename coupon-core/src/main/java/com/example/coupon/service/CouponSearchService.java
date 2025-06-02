package com.example.coupon.service;

import com.example.coupon.dto.CouponResponse;
import com.example.coupon.dto.PageResponse;
import com.example.coupon.entity.Coupon;
import com.example.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CouponSearchService {

    private final CouponRepository couponRepository;

    public PageResponse<CouponResponse> getUserCouponsWithPaging(
            String userId,
            int page,
            int size,
            String sortBy) {

        Sort sort = Sort.by(Sort.Direction.DESC, sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Coupon> couponsPage = couponRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        List<CouponResponse> content = couponsPage.getContent().stream()
                .map(this::convertToCouponResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                couponsPage.getNumber(),
                couponsPage.getSize(),
                couponsPage.getTotalElements(),
                couponsPage.getTotalPages(),
                couponsPage.isFirst(),
                couponsPage.isLast()
        );
    }

    private CouponResponse convertToCouponResponse(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .userId(coupon.getUserId())
                .couponType(coupon.getCouponType())
                .discountAmount(coupon.getDiscountAmount())
                .discountRate(coupon.getDiscountRate())
                .status(coupon.getStatus())
                .issuedAt(coupon.getIssuedAt())
                .expiresAt(coupon.getExpiresAt())
                .usedAt(coupon.getUsedAt())
                .createdAt(coupon.getCreatedAt())
                .updatedAt(coupon.getUpdatedAt())
                .build();
    }
}
