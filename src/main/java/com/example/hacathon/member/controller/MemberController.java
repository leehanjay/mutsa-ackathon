package com.example.hacathon.member.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.example.hacathon.global.auth.JwtUserPrincipal;
import com.example.hacathon.member.dto.request.UpdateBudgetRequestDto;
import com.example.hacathon.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/budget")
    public ApiResponse<String> updateBudget(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody UpdateBudgetRequestDto request
    ) {
        Long memberId = principal.getUserId();

        memberService.updateBudget(memberId, request);

        return ApiResponse.onSuccess(GeneralSuccessCode._OK, "예산 및 기간 설정 완료");
    }
}