package com.example.coupon.repository;

import com.example.coupon.entity.Coupon;
import com.example.coupon.entity.CouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 쿠폰 레포지토리
 * 쿠폰 엔티티에 대한 데이터 액세스를 처리하는 레포지토리
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    
    /**
     * 쿠폰 코드로 쿠폰을 찾습니다.
     * 
     * @param code 쿠폰 코드
     * @return 쿠폰 정보
     */
    Optional<Coupon> findByCode(String code);
    
    /**
     * 사용자의 모든 쿠폰을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 사용자의 쿠폰 목록
     */
    List<Coupon> findByUserId(String userId);
    
    /**
     * 사용자의 특정 상태 쿠폰을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @param status 쿠폰 상태
     * @return 조건에 맞는 쿠폰 목록
     */
    List<Coupon> findByUserIdAndStatus(String userId, CouponStatus status);


    Page<Coupon> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);


    // 상태별 조회도 가능
    Page<Coupon> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, CouponStatus status, Pageable pageable);
}
