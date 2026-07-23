package com.example.hacathon.global.apiPayload.exception;

import com.example.hacathon.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectException extends RuntimeException{
    private final BaseErrorCode code;
}
