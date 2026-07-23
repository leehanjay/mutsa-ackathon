package com.example.hacathon.member.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBudgetRequestDto {

    @NotNull(message = "월급일은 필수입니다.")
    @Min(value = 1, message = "월급일은 1일 이상이어야 합니다.")
    @Max(value = 31, message = "월급일은 31일 이하여야 합니다.")
    private Integer payDay;

    @NotNull(message = "월 예산은 필수입니다.")
    @Positive(message = "월 예산은 1원 이상이어야 합니다.")
    private Integer monthlyBudget;
}