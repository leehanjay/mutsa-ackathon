package com.example.hacathon.global.apiPayload.exception;

import com.example.hacathon.global.apiPayload.ApiResponse;
import com.example.hacathon.global.apiPayload.code.BaseErrorCode;
import com.example.hacathon.global.apiPayload.code.GeneralErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<Object> handleProjectException(ProjectException e, WebRequest request) {
        BaseErrorCode errorCode = e.getCode();
        ApiResponse<Object> response = ApiResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null);
        return handleExceptionInternal(e, response, HttpHeaders.EMPTY, errorCode.getStatus(), request);
    }

    // DTO Validation(@Valid) 실패 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Object> response = ApiResponse.onFailure(
                GeneralErrorCode._BAD_REQUEST.getCode(),
                "유효성 검사 실패",
                errors
        );

        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    // 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e, WebRequest request) {
        ApiResponse<Object> response = ApiResponse.onFailure(
                GeneralErrorCode._INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage(),
                null
        );
        return handleExceptionInternal(e, response, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
