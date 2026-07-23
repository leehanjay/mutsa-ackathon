package com.example.hacathon.expense.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ExpenseRequestDto {
    private String category;
    private Integer amount;
    private LocalDate expenseDate;
    private String memo;
}