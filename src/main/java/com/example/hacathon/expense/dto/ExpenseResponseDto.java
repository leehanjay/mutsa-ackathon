package com.example.hacathon.expense.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ExpenseResponseDto {
    private String id;
    private String merchant;
    private Integer amount;
    private LocalDate date;
    private Integer spentAmount;
}