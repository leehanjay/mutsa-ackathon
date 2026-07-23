package com.example.hacathon.home.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.example.hacathon.home.dto.HomeResponseDto;
import com.example.hacathon.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ApiResponse<HomeResponseDto> getHome() {
        // TODO: JWT 필터 연동 후 교체
        Long memberId = 1L;
        HomeResponseDto response = homeService.getHomeData(memberId);


        return ApiResponse.onSuccess(GeneralSuccessCode._OK, response);
    }
}