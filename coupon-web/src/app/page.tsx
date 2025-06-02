"use client";

import React from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Ticket, Users, TrendingUp, Clock } from "lucide-react";
import Link from "next/link";

export default function Home() {
  const stats = [
    {
      title: "총 발급된 쿠폰",
      value: "1,234",
      description: "지난 30일간",
      icon: Ticket,
      trend: "+12%",
    },
    {
      title: "활성 사용자",
      value: "856",
      description: "현재 활성 상태",
      icon: Users,
      trend: "+8%",
    },
    {
      title: "사용률",
      value: "68%",
      description: "평균 쿠폰 사용률",
      icon: TrendingUp,
      trend: "+5%",
    },
    {
      title: "대기 중인 요청",
      value: "23",
      description: "처리 대기 중",
      icon: Clock,
      trend: "-2%",
    },
  ];

  return (
    <div className="space-y-8">
      {/* 헤더 */}
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">대시보드</h1>
        <p className="text-muted-foreground">
          RabbitMQ를 활용한 쿠폰 발급 시스템의 현황을 확인하세요.
        </p>
      </div>

      {/* 통계 카드 */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {stat.title}
              </CardTitle>
              <stat.icon className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
              <p className="text-xs text-muted-foreground">
                {stat.description}
              </p>
              <div className="text-xs text-green-600 mt-1">
                {stat.trend} from last month
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* 빠른 액션 */}
      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>쿠폰 발급</CardTitle>
            <CardDescription>
              새로운 쿠폰을 발급하여 사용자에게 혜택을 제공하세요.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Link href="/coupon-issue">
              <Button className="w-full">
                <Ticket className="mr-2 h-4 w-4" />
                쿠폰 발급하기
              </Button>
            </Link>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>회원 쿠폰 조회</CardTitle>
            <CardDescription>
              회원별 보유 쿠폰과 사용 내역을 확인하세요.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Link href="/user-coupons">
              <Button variant="outline" className="w-full">
                <Users className="mr-2 h-4 w-4" />
                쿠폰 조회하기
              </Button>
            </Link>
          </CardContent>
        </Card>
      </div>

      {/* RabbitMQ 시스템 설명 */}
      <Card>
        <CardHeader>
          <CardTitle>시스템 아키텍처</CardTitle>
          <CardDescription>
            RabbitMQ를 활용한 비동기 쿠폰 발급 시스템의 동작 방식
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-3">
            <div className="text-center p-4">
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center mx-auto mb-2">
                <span className="text-blue-600 font-bold">1</span>
              </div>
              <h3 className="font-semibold mb-1">요청 수신</h3>
              <p className="text-sm text-muted-foreground">
                사용자가 쿠폰 발급을 요청합니다.
              </p>
            </div>
            <div className="text-center p-4">
              <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center mx-auto mb-2">
                <span className="text-green-600 font-bold">2</span>
              </div>
              <h3 className="font-semibold mb-1">큐 처리</h3>
              <p className="text-sm text-muted-foreground">
                RabbitMQ에서 비동기적으로 처리됩니다.
              </p>
            </div>
            <div className="text-center p-4">
              <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center mx-auto mb-2">
                <span className="text-purple-600 font-bold">3</span>
              </div>
              <h3 className="font-semibold mb-1">결과 반환</h3>
              <p className="text-sm text-muted-foreground">
                처리 결과가 사용자에게 전달됩니다.
              </p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
