package com.example.hacathon.expense.controller;

import com.example.hacathon.expense.dto.ExpenseRequestDto;
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
    public ApiResponse<Long> createExpense(@RequestBody ExpenseRequestDto request) {
        // TODO: 팀원이 JWT 필터(Filter) 및 인가(Auth) 로직을 추가하면 1L을 @AuthenticationPrincipal 등으로 교체!
        Long memberId = 1L;
        Long expenseId = expenseService.createExpense(memberId, request);

        // 💡 수정됨: 팀원의 새로운 ApiResponse 포맷 적용 (_CREATED 사용)
        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, expenseId);
    }
}