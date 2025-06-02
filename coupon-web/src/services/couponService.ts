import {
  CouponRequest,
  CouponResponse,
  Coupon,
  PageRequest,
  CouponPageResponse,
} from "@/types/coupon";

// API URL 설정: 환경 변수에서 가져오거나 기본값 사용
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "";
const API_PATH = "/api";

/**
 * 쿠폰 서비스
 * 백엔드 API와 통신하여 쿠폰 관련 기능을 제공합니다.
 */
export const couponService = {
  /**
   * 쿠폰 발급 요청
   *
   * @param request 쿠폰 발급 요청 정보
   * @returns 발급 결과
   */
  async issueCoupon(request: CouponRequest): Promise<CouponResponse> {
    try {
      const response = await fetch(`${API_BASE_URL}${API_PATH}/coupons/issue`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(
          data.message || "쿠폰 발급 처리 중 오류가 발생했습니다.",
        );
      }

      return data;
    } catch (error) {
      // 오류 응답 생성
      return {
        userId: request.userId,
        couponType: request.couponType,
        success: false,
        message:
          error instanceof Error
            ? error.message
            : "쿠폰 발급 요청 중 오류가 발생했습니다.",
      };
    }
  },

  /**
   * 사용자의 쿠폰 목록 조회
   *
   * @param userId 사용자 ID
   * @returns 쿠폰 목록
   */
  async getUserCoupons(userId: string): Promise<Coupon[]> {
    try {
      const response = await fetch(
        `${API_BASE_URL}${API_PATH}/coupons/user/${userId}`,
      );

      if (!response.ok) {
        throw new Error("쿠폰 목록을 가져오는데 실패했습니다.");
      }

      return await response.json();
    } catch (error) {
      console.error("쿠폰 목록 조회 중 오류 발생:", error);
      return [];
    }
  },

  /**
   * 사용자의 유효한 쿠폰 목록 조회
   *
   * @param userId 사용자 ID
   * @returns 유효한 쿠폰 목록
   */
  async getValidUserCoupons(userId: string): Promise<Coupon[]> {
    try {
      const response = await fetch(
        `${API_BASE_URL}${API_PATH}/coupons/user/${userId}/valid`,
      );

      if (!response.ok) {
        throw new Error("유효한 쿠폰 목록을 가져오는데 실패했습니다.");
      }

      return await response.json();
    } catch (error) {
      console.error("유효 쿠폰 목록 조회 중 오류 발생:", error);
      return [];
    }
  },
  // 페이징으로 사용자 쿠폰 조회
  async getUserCouponsWithPaging(
    userId: string,
    pageRequest: PageRequest,
  ): Promise<CouponPageResponse> {
    const params = new URLSearchParams({
      page: pageRequest.page.toString(),
      size: pageRequest.size.toString(),
      ...(pageRequest.sort && { sort: pageRequest.sort }),
    });

    const response = await fetch(
      `${API_BASE_URL}${API_PATH}/coupons/users/${userId}/paged?${params}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      },
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  },
};
