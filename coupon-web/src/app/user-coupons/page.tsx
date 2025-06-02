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
import { Search, ChevronLeft, ChevronRight } from "lucide-react";
import { CouponPageResponse, PageRequest } from "@/types/coupon";
import { couponService } from "@/services/couponService";
import CouponList from "@/components/CouponList";

export default function UserCouponsPage() {
  const [userId, setUserId] = useState("");
  const [couponData, setCouponData] = useState<CouponPageResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  const handleSearch = async (page: number = 0) => {
    if (!userId.trim()) return;

    setLoading(true);
    setSearched(true);
    setCurrentPage(page);

    try {
      const pageRequest: PageRequest = {
        page,
        size: pageSize,
        sort: "issuedAt",
      };

      const result = await couponService.getUserCouponsWithPaging(
        userId.trim(),
        pageRequest,
      );
      setCouponData(result);
    } catch (error) {
      console.error("쿠폰 조회 실패:", error);
      setCouponData(null);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (newPage: number) => {
    handleSearch(newPage);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    handleSearch(0); // 새로운 검색은 첫 페이지부터
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
          <form onSubmit={handleSubmit} className="flex gap-4">
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
              {!loading && couponData && (
                <span className="ml-2 text-sm font-normal text-muted-foreground">
                  (총 {couponData.totalElements}개)
                </span>
              )}
            </CardTitle>
            <CardDescription>
              사용 가능한 쿠폰과 사용된 쿠폰 내역입니다.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <CouponList coupons={couponData?.content || []} loading={loading} />

            {/* 페이징 컨트롤 */}
            {couponData && couponData.totalPages > 1 && (
              <div className="flex items-center justify-between mt-6">
                <div className="text-sm text-muted-foreground">
                  페이지 {couponData.page + 1} / {couponData.totalPages}
                  (총 {couponData.totalElements}개 항목)
                </div>

                <div className="flex items-center space-x-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={couponData.first || loading}
                  >
                    <ChevronLeft className="h-4 w-4" />
                    이전
                  </Button>

                  <div className="flex items-center space-x-1">
                    {Array.from(
                      { length: Math.min(5, couponData.totalPages) },
                      (_, i) => {
                        const pageNum =
                          Math.max(
                            0,
                            Math.min(
                              couponData.totalPages - 5,
                              couponData.page - 2,
                            ),
                          ) + i;

                        return (
                          <Button
                            key={pageNum}
                            variant={
                              pageNum === couponData.page
                                ? "default"
                                : "outline"
                            }
                            size="sm"
                            onClick={() => handlePageChange(pageNum)}
                            disabled={loading}
                          >
                            {pageNum + 1}
                          </Button>
                        );
                      },
                    )}
                  </div>

                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={couponData.last || loading}
                  >
                    다음
                    <ChevronRight className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      )}
    </div>
  );
}
