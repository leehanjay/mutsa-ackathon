package com.example.hacathon.global.auth.controller;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.example.hacathon.global.auth.service.AuthService;
import com.example.hacathon.member.dto.request.LoginRequestDto;
import com.example.hacathon.member.dto.request.SignupRequestDto;
import com.example.hacathon.member.dto.response.LoginResponseDto;
import com.example.hacathon.member.dto.response.SignupResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {
        SignupResponseDto response = authService.signup(request);
        return ApiResponse.onSuccess(GeneralSuccessCode.SIGNUP_SUCCESS, response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ApiResponse.onSuccess(GeneralSuccessCode.LOGIN_SUCCESS, response);
    }
}
