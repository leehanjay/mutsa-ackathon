package com.example.hacathon.regularpayment.service;

import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
import com.example.hacathon.regularpayment.dto.SubscriptionRequestDto;
import com.example.hacathon.regularpayment.entity.RegularPayment;
import com.example.hacathon.regularpayment.repository.RegularPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegularPaymentService {

    private final RegularPaymentRepository repository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createSubscription(Long memberId, SubscriptionRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 💡 수정됨: C님이 엔티티에 만들어두신 강력한 예외처리 메서드(createNew)를 사용합니다!
        RegularPayment payment = RegularPayment.createNew(
                member,
                request.getPaymentName(),
                request.getAmount(),
                request.getBillingDay()
        );

        return repository.save(payment).getId();
    }

    @Transactional
    public Boolean toggleSubscription(Long paymentId) {
        RegularPayment payment = repository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구독 내역을 찾을 수 없습니다."));

        // 💡 주의: RegularPayment 엔티티 클래스 안에 아래 메서드를 직접 추가해 주셔야 합니다!
        // public void toggleActive() { this.isActive = !this.isActive; }
        payment.toggleActive();

        return payment.getIsActive();
    }
}