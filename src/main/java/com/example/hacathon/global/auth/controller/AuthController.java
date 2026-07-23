package com.example.hacathon.global.auth.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.auth.service.AuthService;
import com.example.hacathon.member.dto.request.LoginRequestDto;
import com.example.hacathon.member.dto.response.LoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ApiResponse.success(200, "로그인 성공", response);
    }
}
