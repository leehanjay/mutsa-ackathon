package com.example.hacathon.regularpayment.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.example.hacathon.global.auth.JwtUserPrincipal;
import com.example.hacathon.regularpayment.dto.SubscriptionRequestDto;
import com.example.hacathon.regularpayment.service.RegularPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class RegularPaymentController {

    private final RegularPaymentService service;

    @PostMapping
    public ApiResponse<Long> createSubscription(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestBody SubscriptionRequestDto request
    ) {
        Long memberId = user.getUserId();
        Long paymentId = service.createSubscription(memberId, request);

        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, paymentId);
    }

    @PatchMapping("/{paymentId}/toggle")
    public ApiResponse<Boolean> toggleSubscription(@PathVariable Long paymentId) {
        Boolean currentStatus = service.toggleSubscription(paymentId);


        return ApiResponse.onSuccess(GeneralSuccessCode._OK, currentStatus);
    }
}