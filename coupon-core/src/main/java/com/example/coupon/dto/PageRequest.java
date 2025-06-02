package com.example.coupon.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageRequest {
    private int page = 0;        // 페이지 번호 (0부터 시작)
    private int size = 10;       // 페이지 크기
    private String sort;         // 정렬 조건 (예: "createdAt,desc")

}