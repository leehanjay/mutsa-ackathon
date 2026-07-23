package com.example.hacathon.global.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtUserPrincipal {
    private final Long userId;
    private final String email;
}
