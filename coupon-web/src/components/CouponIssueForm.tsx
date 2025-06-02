import React, { useState } from "react";
import { CouponRequest, CouponResponse } from "@/types/coupon";

interface CouponIssueFormProps {
  onSubmit: (request: CouponRequest) => Promise<CouponResponse>;
  onSuccess: () => void;
}

/**
 * 쿠폰 발급 폼 컴포넌트
 * 쿠폰 발급 요청 폼을 제공합니다.
 */
const CouponIssueForm: React.FC<CouponIssueFormProps> = ({
  onSubmit,
  onSuccess,
}) => {
  // 쿠폰 유형 목록
  const couponTypes = [
    { value: "DISCOUNT_AMOUNT", label: "금액 할인" },
    { value: "DISCOUNT_RATE", label: "비율 할인" },
  ];

  // 폼 상태
  const [formData, setFormData] = useState<CouponRequest>({
    userId: "",
    couponType: "DISCOUNT_AMOUNT",
    discountAmount: 1000,
    discountRate: 10,
    expiryDays: 30,
  });

  // 폼 제출 상태
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  // 입력 필드 변경 핸들러
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "discountAmount" ||
        name === "discountRate" ||
        name === "expiryDays"
          ? parseInt(value, 10)
          : value,
    }));
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    setSuccess(null);

    try {
      // 사용자 ID 유효성 검사
      if (!formData.userId.trim()) {
        throw new Error("사용자 ID를 입력해주세요.");
      }

      // 쿠폰 발급 요청
      const response = await onSubmit(formData);

      if (response.success) {
        setSuccess(
          `쿠폰이 성공적으로 발급되었습니다. 쿠폰 코드: ${response.code}`,
        );
        // 성공 시 초기화
        setFormData((prev) => ({
          ...prev,
          userId: "",
        }));
        onSuccess();
      } else {
        setError(response.message || "쿠폰 발급에 실패했습니다.");
      }
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "쿠폰 발급 중 오류가 발생했습니다.",
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="card">
      <h2 className="text-xl font-bold mb-4">쿠폰 발급</h2>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {success && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
          {success}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label
            htmlFor="userId"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            사용자 ID
          </label>
          <input
            type="text"
            id="userId"
            name="userId"
            value={formData.userId}
            onChange={handleChange}
            className="input"
            placeholder="사용자 ID를 입력하세요"
            required
          />
        </div>

        <div className="mb-4">
          <label
            htmlFor="couponType"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            쿠폰 유형
          </label>
          <select
            id="couponType"
            name="couponType"
            value={formData.couponType}
            onChange={handleChange}
            className="input"
          >
            {couponTypes.map((type) => (
              <option key={type.value} value={type.value}>
                {type.label}
              </option>
            ))}
          </select>
        </div>

        {formData.couponType === "DISCOUNT_AMOUNT" ? (
          <div className="mb-4">
            <label
              htmlFor="discountAmount"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              할인 금액 (원)
            </label>
            <input
              type="number"
              id="discountAmount"
              name="discountAmount"
              value={formData.discountAmount}
              onChange={handleChange}
              className="input"
              min="0"
              step="500"
              required
            />
          </div>
        ) : (
          <div className="mb-4">
            <label
              htmlFor="discountRate"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              할인율 (%)
            </label>
            <input
              type="number"
              id="discountRate"
              name="discountRate"
              value={formData.discountRate}
              onChange={handleChange}
              className="input"
              min="1"
              max="100"
              required
            />
          </div>
        )}

        <div className="mb-4">
          <label
            htmlFor="expiryDays"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            유효 기간 (일)
          </label>
          <input
            type="number"
            id="expiryDays"
            name="expiryDays"
            value={formData.expiryDays}
            onChange={handleChange}
            className="input"
            min="1"
            required
          />
        </div>

        <div className="mt-6">
          <button
            type="submit"
            className="btn btn-primary"
            disabled={submitting}
          >
            {submitting ? "처리 중..." : "쿠폰 발급하기"}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CouponIssueForm;
