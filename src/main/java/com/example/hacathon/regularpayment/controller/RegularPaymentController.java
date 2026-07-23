package com.example.hacathon.regularpayment.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.regularpayment.dto.SubscriptionRequestDto;
import com.example.hacathon.regularpayment.service.RegularPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class RegularPaymentController {

    private final RegularPaymentService service;

    @PostMapping
    public ApiResponse<Long> createSubscription(@RequestBody SubscriptionRequestDto request) {
        Long memberId = 1L; // 하드코딩
        Long paymentId = service.createSubscription(memberId, request);
        return ApiResponse.success(200, "정기결제 등록 성공", paymentId);
    }

    @PatchMapping("/{paymentId}/toggle")
    public ApiResponse<Boolean> toggleSubscription(@PathVariable Long paymentId) {
        Boolean currentStatus = service.toggleSubscription(paymentId);
        return ApiResponse.success(200, "정기결제 활성화 상태 변경 성공", currentStatus);
    }
}