package com.example.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class PageResponse<T> {
    private List<T> content;     // 실제 데이터
    private int page;            // 현재 페이지
    private int size;            // 페이지 크기
    private long totalElements;  // 전체 데이터 수
    private int totalPages;      // 전체 페이지 수
    private boolean first;       // 첫 페이지 여부
    private boolean last;        // 마지막 페이지 여부

}