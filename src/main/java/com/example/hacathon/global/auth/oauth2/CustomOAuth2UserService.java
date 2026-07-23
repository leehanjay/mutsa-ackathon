package com.example.hacathon.global.auth.oauth2;

import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 카카오 정보 파싱
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        String email = kakaoUserInfo.getEmail();
        String nickname = kakaoUserInfo.getNickname();

        // DB에서 회원 조회 또는 자동 회원가입
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("새로운 카카오 유저 자동 회원가입: {}", email);
                    Member newMember = Member.createSocial(email, nickname);
                    return memberRepository.save(newMember);
                });

        // SuccessHandler에서 사용할 수 있도록 속성에 memberId 추가
        Map<String, Object> customAttributes = new HashMap<>(oAuth2User.getAttributes());
        customAttributes.put("memberId", member.getId());
        customAttributes.put("email", member.getEmail());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "id" // 카카오의 PK 필드명
        );
    }
}