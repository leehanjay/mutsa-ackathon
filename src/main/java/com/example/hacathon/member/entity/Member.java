package com.example.hacathon.member.entity;

import com.example.hacathon.basetimeentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String password; // 소셜 로그인 계정일 경우 null 허용

    @Column(nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "character_type", nullable = false, length = 30)
    private CharacterType characterType;

    @Column(name = "pay_day")
    private Integer payDay;

    @Column(name = "monthly_budget")
    private Integer monthlyBudget;

    @Builder
    public Member(String email, String password, String nickname, CharacterType characterType, Integer payDay, Integer monthlyBudget) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.characterType = characterType != null ? characterType : CharacterType.DEFAULT;
        this.payDay = payDay;
        this.monthlyBudget = monthlyBudget;
    }

    public static Member createGeneral(String email, String password, String nickname) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수 입력값입니다.");
        }

        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .characterType(CharacterType.DEFAULT)
                .build();
    }

    // 2️⃣ 소셜 회원 생성 정적 팩토리 메서드 (비밀번호 없음)
    public static Member createSocial(String email, String nickname) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수 입력값입니다.");
        }

        return Member.builder()
                .email(email)
                .password(null) // 소셜 로그인은 비밀번호 없음
                .nickname(nickname)
                .characterType(CharacterType.DEFAULT)
                .build();
    }

    // 5번 플랜 세우기(온보딩) 로직
    public void updatePlan(Integer payDay, Integer monthlyBudget, CharacterType characterType) {
        this.payDay = payDay;
        this.monthlyBudget = monthlyBudget;
        this.characterType = characterType;
    }
}