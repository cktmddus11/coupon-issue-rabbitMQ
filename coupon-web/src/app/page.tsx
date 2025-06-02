"use client";

import React, { useState, useEffect } from "react";
import { CouponRequest, Coupon } from "@/types/coupon";
import { couponService } from "@/services/couponService";
import CouponIssueForm from "@/components/CouponIssueForm";
import CouponList from "@/components/CouponList";

/**
 * 메인 페이지 컴포넌트
 * 쿠폰 발급 및 목록 조회 기능을 제공합니다.
 */
export default function Home() {
  // 쿠폰 목록 상태
  const [coupons, setCoupons] = useState<Coupon[]>([]);
  const [loading, setLoading] = useState(false);
  const [userId, setUserId] = useState<string>("");

  // 컴포넌트 마운트 시 기본 사용자 ID 설정
  useEffect(() => {
    // 로컬 스토리지에서 최근 사용자 ID 조회
    const savedUserId = localStorage.getItem("lastUserId") || "";
    setUserId(savedUserId);

    // 저장된 사용자 ID가 있으면 쿠폰 목록 조회
    if (savedUserId) {
      fetchCoupons(savedUserId);
    }
  }, []);

  // 쿠폰 목록 조회 함수
  const fetchCoupons = async (id: string) => {
    if (!id) return;

    setLoading(true);
    try {
      const userCoupons = await couponService.getUserCoupons(id);
      setCoupons(userCoupons);
    } catch (error) {
      console.error("쿠폰 목록 조회 실패:", error);
    } finally {
      setLoading(false);
    }
  };

  // 쿠폰 발급 요청 처리 함수
  const handleCouponIssue = async (request: CouponRequest) => {
    // 사용자 ID 저장
    localStorage.setItem("lastUserId", request.userId);
    setUserId(request.userId);

    // 쿠폰 발급 요청
    return couponService.issueCoupon(request);
  };

  // 쿠폰 발급 성공 시 처리 함수
  const handleIssueSuccess = () => {
    // 쿠폰 목록 다시 조회
    fetchCoupons(userId);
  };

  return (
    <main className="container py-8">
      <header className="mb-8 text-center">
        <h1 className="text-3xl font-bold text-primary-800">
          RabbitMQ를 활용한 쿠폰 발급 시스템
        </h1>
        <p className="mt-2 text-gray-600">
          RabbitMQ를 사용하여 비동기적으로 쿠폰을 발급하는 시스템입니다.
        </p>
      </header>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* 왼쪽: 쿠폰 발급 폼 */}
        <div className="lg:col-span-1">
          <CouponIssueForm
            onSubmit={handleCouponIssue}
            onSuccess={handleIssueSuccess}
          />

          <div className="mt-6 bg-blue-50 p-4 rounded-lg">
            <h3 className="font-bold text-blue-800 mb-2">RabbitMQ 흐름 설명</h3>
            <ol className="list-decimal pl-5 text-sm text-blue-700">
              <li className="mb-1">사용자가 쿠폰 발급을 요청합니다.</li>
              <li className="mb-1">요청이 RabbitMQ의 요청 큐로 전송됩니다.</li>
              <li className="mb-1">
                Worker가 요청을 수신하여 쿠폰을 발급합니다.
              </li>
              <li className="mb-1">처리 결과가 응답 큐로 다시 전송됩니다.</li>
              <li className="mb-1">
                API 서버가 응답을 수신하여 클라이언트에게 전달합니다.
              </li>
            </ol>
          </div>
        </div>

        {/* 오른쪽: 쿠폰 목록 */}
        <div className="lg:col-span-2">
          {!userId ? (
            <div className="bg-gray-50 p-6 rounded-lg text-center">
              <p className="text-gray-500">
                쿠폰을 발급하면 목록이 여기에 표시됩니다.
              </p>
            </div>
          ) : (
            <>
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-xl font-bold">{userId}님의 쿠폰</h2>
                <button
                  onClick={() => fetchCoupons(userId)}
                  className="btn btn-secondary text-sm"
                  disabled={loading}
                >
                  {loading ? "로딩 중..." : "새로고침"}
                </button>
              </div>
              <CouponList coupons={coupons} loading={loading} />
            </>
          )}
        </div>
      </div>
    </main>
  );
}
