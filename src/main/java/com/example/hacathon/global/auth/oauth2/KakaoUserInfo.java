package com.example.hacathon.global.auth.oauth2;

import java.util.Map;

public class KakaoUserInfo {
    private final Map<String, Object> attributes;
    private final Map<String, Object> attributesAccount;
    private final Map<String, Object> attributesProfile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }

    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    // 이메일 권한이 없으므로 카카오 고유 ID를 이용해 서비스 내 고유 이메일 생성
    public String getEmail() {
        return getProviderId() + "@kakao.social";
    }

    public String getNickname() {
        if (attributesProfile == null || attributesProfile.get("nickname") == null) {
            return "카카오유저";
        }
        return (String) attributesProfile.get("nickname");
    }
}