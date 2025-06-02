import React from 'react';
import { Coupon, CouponStatus } from '@/types/coupon';

interface CouponListProps {
  coupons: Coupon[];
  loading: boolean;
}

/**
 * 쿠폰 목록 컴포넌트
 * 사용자의 쿠폰 목록을 표시합니다.
 */
const CouponList: React.FC<CouponListProps> = ({ coupons, loading }) => {
  // 쿠폰 상태에 따른 배경색 반환
  const getStatusColor = (status: CouponStatus): string => {
    switch (status) {
      case CouponStatus.ISSUED:
        return 'bg-green-100 text-green-800';
      case CouponStatus.USED:
        return 'bg-gray-100 text-gray-800';
      case CouponStatus.EXPIRED:
        return 'bg-red-100 text-red-800';
      case CouponStatus.CANCELLED:
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  // 쿠폰 상태 한글 표시
  const getStatusText = (status: CouponStatus): string => {
    switch (status) {
      case CouponStatus.ISSUED:
        return '사용 가능';
      case CouponStatus.USED:
        return '사용 완료';
      case CouponStatus.EXPIRED:
        return '만료됨';
      case CouponStatus.CANCELLED:
        return '취소됨';
      default:
        return '알 수 없음';
    }
  };

  // 날짜 포맷팅
  const formatDate = (dateString?: string): string => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-10">
        <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="mt-6">
      <h2 className="text-xl font-bold mb-4">내 쿠폰 목록</h2>
      
      {coupons.length === 0 ? (
        <div className="text-center py-10 bg-gray-50 rounded-lg">
          <p className="text-gray-500">발급된 쿠폰이 없습니다.</p>
        </div>
      ) : (
        <div className="overflow-hidden shadow ring-1 ring-black ring-opacity-5 md:rounded-lg">
          <table className="min-w-full divide-y divide-gray-300">
            <thead className="bg-gray-50">
              <tr>
                <th scope="col" className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">쿠폰 코드</th>
                <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">쿠폰 유형</th>
                <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">할인 정보</th>
                <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">발급일</th>
                <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">만료일</th>
                <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">상태</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200 bg-white">
              {coupons.map((coupon) => (
                <tr key={coupon.id}>
                  <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                    {coupon.code}
                  </td>
                  <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                    {coupon.couponType}
                  </td>
                  <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                    {coupon.discountAmount 
                      ? `${coupon.discountAmount.toLocaleString()}원`
                      : coupon.discountRate
                      ? `${coupon.discountRate}% 할인`
                      : '-'}
                  </td>
                  <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                    {formatDate(coupon.issuedAt)}
                  </td>
                  <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                    {formatDate(coupon.expiresAt)}
                  </td>
                  <td className="whitespace-nowrap px-3 py-4 text-sm">
                    <span className={`px-2 py-1 rounded-full text-xs ${getStatusColor(coupon.status)}`}>
                      {getStatusText(coupon.status)}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default CouponList;
