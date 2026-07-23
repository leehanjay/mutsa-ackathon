package com.example.hacathon.global.auth.service;

import com.example.hacathon.global.apiPayload.code.GeneralErrorCode;
import com.example.hacathon.global.apiPayload.exception.ProjectException;
import com.example.hacathon.global.auth.JwtTokenProvider;
import com.example.hacathon.member.dto.request.LoginRequestDto;
import com.example.hacathon.member.dto.request.SignupRequestDto;
import com.example.hacathon.member.dto.response.LoginResponseDto;
import com.example.hacathon.member.dto.response.SignupResponseDto;
import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
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

    @Transactional
    public SignupResponseDto signup(SignupRequestDto request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ProjectException(GeneralErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.createGeneral(
                request.getEmail(),
                encodedPassword,
                request.getNickname()
        );

        Member savedMember = memberRepository.save(member);
        return SignupResponseDto.from(savedMember);
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ProjectException(GeneralErrorCode.MEMBER_NOT_FOUND));

        if (member.getPassword() == null) {
            throw new ProjectException(GeneralErrorCode.SOCIAL_MEMBER_CANNOT_LOGIN);
        }

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new ProjectException(GeneralErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createToken(member.getId(), member.getEmail());
        return LoginResponseDto.of(accessToken, 36000L);
    }
}
