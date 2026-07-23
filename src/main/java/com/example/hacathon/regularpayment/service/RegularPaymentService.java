package com.example.hacathon.regularpayment.service;

import com.example.hacathon.global.apiPayload.code.GeneralErrorCode;
import com.example.hacathon.global.apiPayload.exception.ProjectException;
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
                .orElseThrow(() -> new ProjectException(GeneralErrorCode.MEMBER_NOT_FOUND));

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
                .orElseThrow(() -> new ProjectException(GeneralErrorCode._BAD_REQUEST));

        payment.toggleActive();

        return payment.getIsActive();
    }
}