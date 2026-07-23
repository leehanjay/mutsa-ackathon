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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto request
    ) {
        SignupResponseDto result = authService.signup(request);

        ApiResponse<SignupResponseDto> response =
                ApiResponse.onSuccess(
                        GeneralSuccessCode.SIGNUP_SUCCESS,
                        result
                );

        return ResponseEntity
                .status(GeneralSuccessCode.SIGNUP_SUCCESS.getStatus())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        LoginResponseDto result = authService.login(request);

        ApiResponse<LoginResponseDto> response =
                ApiResponse.onSuccess(
                        GeneralSuccessCode.LOGIN_SUCCESS,
                        result
                );

        return ResponseEntity
                .status(GeneralSuccessCode.LOGIN_SUCCESS.getStatus())
                .body(response);
    }
}