package com.example.hacathon.global.apiPayload;

import com.example.hacathon.global.apiPayload.code.BaseErrorCode;
import com.example.hacathon.global.apiPayload.code.BaseSuccessCode;
import com.example.hacathon.global.apiPayload.code.GeneralSuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T>{
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 성공 응답 (기본 OK)
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, GeneralSuccessCode._OK.getCode(), GeneralSuccessCode._OK.getMessage(), result);
    }

    // 성공 응답 (커스텀 SuccessCode 지정)
    public static <T> ApiResponse<T> onSuccess(BaseSuccessCode code, T result) {
        return new ApiResponse<>(true, code.getCode(), code.getMessage(), result);
    }

    // 실패 응답
    public static <T> ApiResponse<T> onFailure(String code, String message, T result) {
        return new ApiResponse<>(false, code, message, result);
    }

    public static <T> ApiResponse<T> onFailure(BaseErrorCode code, T result) {
        return new ApiResponse<>(false, code.getCode(), code.getMessage(), result);
    }
}
