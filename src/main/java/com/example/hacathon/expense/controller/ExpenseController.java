package com.example.hacathon.expense.controller;

import com.example.hacathon.expense.dto.ExpenseRequestDto;
import com.example.hacathon.expense.dto.ExpenseResponseDto;
import com.example.hacathon.expense.service.ExpenseService;
import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ApiResponse<ExpenseResponseDto> createExpense(@RequestBody ExpenseRequestDto request) {
        // TODO: JWT 필터 연동 후 @AuthenticationPrincipal 등으로 교체
        Long memberId = 1L;


        ExpenseResponseDto response = expenseService.createExpense(memberId, request);

        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, response);
    }
}