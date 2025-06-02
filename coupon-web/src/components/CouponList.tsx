import React from "react";
import { Coupon, CouponStatus } from "@/types/coupon";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Loader2 } from "lucide-react";

interface CouponListProps {
  coupons: Coupon[];
  loading: boolean;
}

/**
 * 쿠폰 목록 컴포넌트
 * 사용자의 쿠폰 목록을 표시합니다.
 */
const CouponList: React.FC<CouponListProps> = ({ coupons = [], loading }) => {
  // 쿠폰 상태에 따른 뱃지 variant 반환
  const getStatusVariant = (status: CouponStatus) => {
    switch (status) {
      case CouponStatus.ISSUED:
        return "default";
      case CouponStatus.USED:
        return "secondary";
      case CouponStatus.EXPIRED:
        return "destructive";
      case CouponStatus.CANCELLED:
        return "outline";
      default:
        return "secondary";
    }
  };

  // 쿠폰 상태 한글 표시
  const getStatusText = (status: CouponStatus): string => {
    switch (status) {
      case CouponStatus.ISSUED:
        return "사용 가능";
      case CouponStatus.USED:
        return "사용 완료";
      case CouponStatus.EXPIRED:
        return "만료됨";
      case CouponStatus.CANCELLED:
        return "취소됨";
      default:
        return "알 수 없음";
    }
  };

  // 날짜 포맷팅
  const formatDate = (dateString?: string): string => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  // 쿠폰 유형 한글 표시
  const getCouponTypeText = (type: string): string => {
    switch (type) {
      case "DISCOUNT_AMOUNT":
        return "금액 할인";
      case "DISCOUNT_RATE":
        return "비율 할인";
      default:
        return type;
    }
  };

  if (loading) {
    return (
      <Card>
        <CardContent className="flex justify-center items-center py-10">
          <Loader2 className="h-8 w-8 animate-spin" />
          <span className="ml-2">쿠폰 목록을 불러오는 중...</span>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>쿠폰 목록</CardTitle>
        <CardDescription>
          발급된 쿠폰 목록과 사용 현황을 확인하세요.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {coupons.length === 0 ? (
          <div className="text-center py-10">
            <p className="text-muted-foreground">발급된 쿠폰이 없습니다.</p>
          </div>
        ) : (
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>쿠폰 코드</TableHead>
                  <TableHead>유형</TableHead>
                  <TableHead>할인 정보</TableHead>
                  <TableHead>발급일</TableHead>
                  <TableHead>만료일</TableHead>
                  <TableHead>상태</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {coupons.map((coupon) => (
                  <TableRow key={coupon.id}>
                    <TableCell className="font-medium">{coupon.code}</TableCell>
                    <TableCell>
                      {getCouponTypeText(coupon.couponType)}
                    </TableCell>
                    <TableCell>
                      {coupon.discountAmount
                        ? `${coupon.discountAmount.toLocaleString()}원`
                        : coupon.discountRate
                          ? `${coupon.discountRate}% 할인`
                          : "-"}
                    </TableCell>
                    <TableCell>{formatDate(coupon.issuedAt)}</TableCell>
                    <TableCell>{formatDate(coupon.expiresAt)}</TableCell>
                    <TableCell>
                      <Badge variant={getStatusVariant(coupon.status)}>
                        {getStatusText(coupon.status)}
                      </Badge>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        )}
      </CardContent>
    </Card>
  );
};

export default CouponList;
