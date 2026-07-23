package com.example.hacathon.expense.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpenseRequestDto {
    private String merchant; // 프론트 요청에 맞춰 필드명 변경
    private Integer amount;
    // 날짜와 메모는 서버에서 처리하므로 삭제
}