"use client";

import React, { useState } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { CouponRequest } from "@/types/coupon";
import { couponService } from "@/services/couponService";
import CouponIssueForm from "@/components/CouponIssueForm";

export default function CouponIssuePage() {
  const [isLoading, setIsLoading] = useState(false);

  const handleCouponIssue = async (request: CouponRequest) => {
    setIsLoading(true);
    try {
      return await couponService.issueCoupon(request);
    } finally {
      setIsLoading(false);
    }
  };

  const handleIssueSuccess = () => {
    // 성공 처리 로직
    console.log("쿠폰 발급 성공!");
  };

  return (
    <div className="space-y-8">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">쿠폰 발급</h1>
        <p className="text-muted-foreground">
          새로운 쿠폰을 발급하여 사용자에게 혜택을 제공하세요.
        </p>
      </div>

      <div className="grid gap-8 md:grid-cols-2">
        <div>
          <CouponIssueForm
            onSubmit={handleCouponIssue}
            onSuccess={handleIssueSuccess}
          />
        </div>

        <Card>
          <CardHeader>
            <CardTitle>발급 프로세스</CardTitle>
            <CardDescription>
              쿠폰 발급이 어떻게 처리되는지 확인하세요.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="flex items-start space-x-3">
                <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0">
                  <span className="text-blue-600 text-sm font-semibold">1</span>
                </div>
                <div>
                  <h4 className="font-medium">요청 생성</h4>
                  <p className="text-sm text-muted-foreground">
                    사용자 ID와 쿠폰 정보로 발급 요청을 생성합니다.
                  </p>
                </div>
              </div>
              <div className="flex items-start space-x-3">
                <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center flex-shrink-0">
                  <span className="text-green-600 text-sm font-semibold">
                    2
                  </span>
                </div>
                <div>
                  <h4 className="font-medium">큐 전송</h4>
                  <p className="text-sm text-muted-foreground">
                    RabbitMQ를 통해 비동기적으로 처리됩니다.
                  </p>
                </div>
              </div>
              <div className="flex items-start space-x-3">
                <div className="w-8 h-8 bg-purple-100 rounded-full flex items-center justify-center flex-shrink-0">
                  <span className="text-purple-600 text-sm font-semibold">
                    3
                  </span>
                </div>
                <div>
                  <h4 className="font-medium">쿠폰 생성</h4>
                  <p className="text-sm text-muted-foreground">
                    시스템에서 쿠폰을 생성하고 사용자에게 할당합니다.
                  </p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
