package com.example.hacathon.member.service;

import com.example.hacathon.member.dto.request.UpdateBudgetRequestDto;
import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateBudget(Long memberId, UpdateBudgetRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 💡 엔티티에 만들어둔 메서드를 호출하여 더티 체킹(Dirty Checking)으로 안전하게 DB 업데이트!
        member.updateBudgetInfo(request.getPayDay(), request.getMonthlyBudget());
    }
}