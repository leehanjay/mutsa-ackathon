package com.example.hacathon.home.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
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
        // 💡 주의: 인증/인가 구현 전까지는 DB에 넣어둔 1번 유저(텅장이)의 데이터를 고정으로 응답합니다!
        Long memberId = 1L;
        HomeResponseDto response = homeService.getHomeData(memberId);

        // ApiResponse 클래스에 맞춰 status, message, data를 순서대로 넣어줍니다.
        return ApiResponse.success(200, "메인 홈 화면 조회 성공", response);
    }
}