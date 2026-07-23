package com.example.hacathon.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum GeneralSuccessCode implements BaseSuccessCode{
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "성공적으로 생성되었습니다."),

    // 인증/회원 성공 응답
    SIGNUP_SUCCESS(HttpStatus.CREATED, "AUTH201", "회원가입이 성공적으로 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH200", "로그인 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
