"use client";

import React, { useState } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Search } from "lucide-react";
import { Coupon } from "@/types/coupon";
import { couponService } from "@/services/couponService";
import CouponList from "@/components/CouponList";

export default function UserCouponsPage() {
  const [userId, setUserId] = useState("");
  const [coupons, setCoupons] = useState<Coupon[]>([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!userId.trim()) return;

    setLoading(true);
    setSearched(true);
    try {
      const userCoupons = await couponService.getUserCoupons(userId.trim());
      setCoupons(userCoupons);
    } catch (error) {
      console.error("쿠폰 조회 실패:", error);
      setCoupons([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">회원 쿠폰 조회</h1>
        <p className="text-muted-foreground">
          회원 ID를 입력하여 보유 쿠폰과 사용 내역을 확인하세요.
        </p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>사용자 검색</CardTitle>
          <CardDescription>
            조회하고 싶은 사용자의 ID를 입력하세요.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSearch} className="flex gap-4">
            <div className="flex-1">
              <Label htmlFor="userId" className="sr-only">
                사용자 ID
              </Label>
              <Input
                id="userId"
                placeholder="사용자 ID를 입력하세요"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                disabled={loading}
              />
            </div>
            <Button type="submit" disabled={loading || !userId.trim()}>
              <Search className="mr-2 h-4 w-4" />
              {loading ? "조회 중..." : "조회"}
            </Button>
          </form>
        </CardContent>
      </Card>

      {searched && (
        <Card>
          <CardHeader>
            <CardTitle>
              {userId}님의 쿠폰 목록
              {!loading && (
                <span className="ml-2 text-sm font-normal text-muted-foreground">
                  ({coupons.length}개)
                </span>
              )}
            </CardTitle>
            <CardDescription>
              사용 가능한 쿠폰과 사용된 쿠폰 내역입니다.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <CouponList coupons={coupons} loading={loading} />
          </CardContent>
        </Card>
      )}
    </div>
  );
}
