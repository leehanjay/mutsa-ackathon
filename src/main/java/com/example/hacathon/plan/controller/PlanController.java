package com.example.hacathon.plan.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.example.hacathon.global.auth.JwtUserPrincipal;
import com.example.hacathon.member.dto.request.UpdateBudgetRequestDto;
import com.example.hacathon.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plans")
public class PlanController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPlan(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody UpdateBudgetRequestDto request
    ) {
        memberService.updateBudget(
                principal.getUserId(),
                request
        );

        return ResponseEntity
                .status(GeneralSuccessCode._CREATED.getStatus())
                .body(
                        ApiResponse.onSuccess(
                                GeneralSuccessCode._CREATED,
                                "플랜 설정 완료"
                        )
                );
    }
}
