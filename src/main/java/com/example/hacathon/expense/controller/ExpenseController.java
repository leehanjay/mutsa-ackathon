package com.example.hacathon.expense.controller;

import com.example.hacathon.expense.dto.ExpenseRequestDto;
import com.example.hacathon.expense.service.ExpenseService;
import com.example.hacathon.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ApiResponse<Long> createExpense(@RequestBody ExpenseRequestDto request) {
        Long memberId = 1L; // JWT 적용 전까지 1번 유저 하드코딩
        Long expenseId = expenseService.createExpense(memberId, request);
        return ApiResponse.success(200, "지출 내역 등록 성공", expenseId);
    }
}