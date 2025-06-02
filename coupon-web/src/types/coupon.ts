// 쿠폰 상태 열거형
export enum CouponStatus {
  ISSUED = 'ISSUED',
  USED = 'USED',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED'
}

// 쿠폰 발급 요청 인터페이스
export interface CouponRequest {
  userId: string;
  couponType: string;
  discountAmount?: number;
  discountRate?: number;
  expiryDays?: number;
}

// 쿠폰 발급 응답 인터페이스
export interface CouponResponse {
  id?: number;
  code?: string;
  userId: string;
  couponType: string;
  discountAmount?: number;
  discountRate?: number;
  issuedAt?: string;
  expiresAt?: string;
  status?: CouponStatus;
  message: string;
  success: boolean;
}

// 쿠폰 인터페이스
export interface Coupon {
  id: number;
  code: string;
  userId: string;
  couponType: string;
  discountAmount?: number;
  discountRate?: number;
  issuedAt: string;
  expiresAt?: string;
  usedAt?: string;
  status: CouponStatus;
}
