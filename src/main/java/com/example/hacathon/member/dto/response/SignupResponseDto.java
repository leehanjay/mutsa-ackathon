package com.example.hacathon.member.dto.response;

import com.example.hacathon.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponseDto {
    private Long userId;
    private String email;
    private String nickname;

    public static SignupResponseDto from(Member member) {
        return SignupResponseDto.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
