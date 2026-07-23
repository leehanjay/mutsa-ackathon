package com.example.hacathon.home.service;

import com.example.hacathon.expense.repository.ExpenseRepository;
import com.example.hacathon.home.dto.HomeResponseDto;
import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
import com.example.hacathon.regularpayment.entity.RegularPayment;
import com.example.hacathon.regularpayment.repository.RegularPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final MemberRepository memberRepository;
    private final ExpenseRepository expenseRepository;
    private final RegularPaymentRepository regularPaymentRepository;

    public HomeResponseDto getHomeData(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();
        int payDay = member.getPayDay();

        // 1. 월급일 D-Day 및 소비 집계 시작일 계산
        int dDayToPayDay;
        LocalDate startDate;

        if (payDay > currentDay) {
            dDayToPayDay = payDay - currentDay;
            startDate = today.minusMonths(1).withDayOfMonth(payDay); // 지난달 월급일
        } else if (payDay < currentDay) {
            dDayToPayDay = today.lengthOfMonth() - currentDay + payDay;
            startDate = today.withDayOfMonth(payDay); // 이번달 월급일
        } else {
            dDayToPayDay = 0; // 오늘이 월급날 당일
            startDate = today.withDayOfMonth(payDay);
        }

        // 0으로 나누기 방지 (오늘이 월급날이면 남은 일수를 1로 취급)
        int calculationDDay = (dDayToPayDay == 0) ? 1 : dDayToPayDay;

        // 2. 예산 및 하루 권장 소비액 계산
        int totalSpent = expenseRepository.sumAmountByMemberIdAndDateRange(member.getId(), startDate, today);
        int remainingBudget = member.getMonthlyBudget() - totalSpent;
        int dailySafeAmount = remainingBudget / calculationDDay;

        // 3. 캐릭터 상태 및 메시지 결정
        String type = "FLEX";
        String message = "아직 여유롭네요! 계획적인 소비 최고예요!";

        if (dailySafeAmount < 5000) {
            type = "EMPTY_ACCOUNT";
            message = "🚨 이대로 쓰면 유럽 여행은커녕 파산이에요! 오늘은 무조건 집밥 드세요!";
        } else if (dailySafeAmount < 15000) {
            type = "WARNING";
            message = "조금 위험해요! 쇼핑은 참아볼까요?";
        }

        // 4. 가장 가까운 정기결제 찾기
        List<RegularPayment> subscriptions = regularPaymentRepository.findAllByMemberIdAndIsActiveTrue(member.getId());
        HomeResponseDto.UpcomingSubscriptionDto upcomingDto = null;
        int minSubDday = Integer.MAX_VALUE;

        for (RegularPayment sub : subscriptions) {
            int subDay = sub.getBillingDay();
            int subDday = (subDay >= currentDay) ? (subDay - currentDay) : (today.lengthOfMonth() - currentDay + subDay);

            if (subDday < minSubDday) {
                minSubDday = subDday;
                upcomingDto = HomeResponseDto.UpcomingSubscriptionDto.builder()
                        .paymentId(sub.getId())
                        .paymentName(sub.getPaymentName())
                        .amount(sub.getAmount())
                        .billingDay(subDay)
                        .dDayToBilling(subDday)
                        .build();
            }
        }

        // 5. 최종 데이터 응답 조립
        return HomeResponseDto.builder()
                .nickname(member.getNickname())
                .payDay(payDay)
                .dDayToPayDay(dDayToPayDay)
                .monthlyBudget(member.getMonthlyBudget())
                .totalSpentThisMonth(totalSpent)
                .remainingBudget(remainingBudget)
                .dailySafeAmount(dailySafeAmount)
                .characterType(type)
                .characterMessage(message)
                .upcomingSubscription(upcomingDto)
                .build();
    }
}