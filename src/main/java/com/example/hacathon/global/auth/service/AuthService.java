package com.example.hacathon.global.auth.service;

import com.example.hacathon.global.auth.JwtTokenProvider;
import com.example.hacathon.member.dto.request.LoginRequestDto;
import com.example.hacathon.member.dto.response.LoginResponseDto;
import com.example.hacathon.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponseDto login(LoginRequestDto request) {
        // 1. 이메일 존재 여부 확인
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 2. 소셜 로그인 회원 체크 (비밀번호가 null인 경우)
        if (member.getPassword() == null) {
            throw new IllegalArgumentException("소셜 로그인으로 가입된 계정입니다. 소셜 로그인을 이용해주세요.");
        }

        // 3. 비밀번호 일치 여부 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 4. JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(member.getId(), member.getEmail());

        // 5. 응답 반환 (유효기간: 36000초 = 10시간)
        return LoginResponseDto.of(accessToken, 36000L);
    }
}
