package com.example.hacathon.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의하세요."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Auth / Member 도메인 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "AUTH400_1", "이미 가입된 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH400_2", "비밀번호가 일치하지 않습니다."),
    SOCIAL_MEMBER_CANNOT_LOGIN(HttpStatus.BAD_REQUEST, "AUTH400_3", "소셜 로그인으로 가입된 계정입니다. 소셜 로그인을 이용해주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
