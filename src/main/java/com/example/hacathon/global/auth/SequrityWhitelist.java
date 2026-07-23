package com.example.hacathon.global.auth;

public class SequrityWhitelist {
    public static final String[] WHITE_LIST = {
            "/api/v1/auth/**",      // 회원가입, 로그인
            "/swagger-ui/**",       // 스웨거 (사용 시)
            "/v3/api-docs/**",
            "/error"
    };
}
