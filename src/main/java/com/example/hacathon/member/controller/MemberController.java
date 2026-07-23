package com.example.hacathon.member.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.example.hacathon.member.dto.request.UpdateBudgetRequestDto;
import com.example.hacathon.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/budget")
    public ApiResponse<String> updateBudget(@RequestBody UpdateBudgetRequestDto request) {
        // TODO: JWT 필터 연동 후 @AuthenticationPrincipal 등으로 교체
        Long memberId = 1L;

        memberService.updateBudget(memberId, request);

        return ApiResponse.onSuccess(GeneralSuccessCode._OK, "예산 및 기간 설정 완료");
    }
}