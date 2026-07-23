package com.example.hacathon.expense.controller;

import com.example.hacathon.expense.dto.ExpenseListResponseDto;
import com.example.hacathon.expense.dto.ExpenseRequestDto;
import com.example.hacathon.expense.dto.ExpenseResponseDto;
import com.example.hacathon.expense.service.ExpenseService;
import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.example.hacathon.global.auth.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ApiResponse<ExpenseResponseDto> createExpense(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody ExpenseRequestDto request
    ) {
        Long memberId = principal.getUserId();
        ExpenseResponseDto response = expenseService.createExpense(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, response);
    }

    @GetMapping
    public ApiResponse<ExpenseListResponseDto> getExpenseList(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long memberId = principal.getUserId();
        ExpenseListResponseDto response = expenseService.getExpenseList(memberId);
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, response);
    }

    @DeleteMapping("/{expenseId}")
    public ApiResponse<Void> deleteExpense(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long expenseId
    ) {
        Long memberId = principal.getUserId();
        expenseService.deleteExpense(memberId, expenseId);
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, null);
    }
}