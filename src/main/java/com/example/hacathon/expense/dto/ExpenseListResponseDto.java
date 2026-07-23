package com.example.hacathon.expense.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ExpenseListResponseDto {
    private Integer totalSpentAmount; // 1번: 이번달 총지출액
    private List<ExpenseItemDto> expenses; // 2번: 소비 리스트

    @Getter
    @Builder
    public static class ExpenseItemDto {
        private Long id;          // 3번 삭제를 위한 ID
        private String merchant;  // 장소 (쿠팡 등)
        private Integer amount;   // 금액 (5000원)
        private LocalDate date;   // 날짜 (2026-01-01)
    }
}