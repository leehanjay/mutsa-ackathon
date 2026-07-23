package com.example.hacathon.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBudgetRequestDto {
    private Integer payDay;
    private Integer monthlyBudget;
}