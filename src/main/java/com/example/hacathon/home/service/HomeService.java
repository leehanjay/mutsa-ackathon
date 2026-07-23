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
            startDate = today.minusMonths(1).withDayOfMonth(payDay);
        } else if (payDay < currentDay) {
            dDayToPayDay = today.lengthOfMonth() - currentDay + payDay;
            startDate = today.withDayOfMonth(payDay);
        } else {
            dDayToPayDay = 0;
            startDate = today.withDayOfMonth(payDay);
        }

        int calculationDDay = (dDayToPayDay == 0) ? 1 : dDayToPayDay;

        // 2. 예산 및 하루 권장 소비액 계산
        int totalSpent = expenseRepository.sumAmountByMemberIdAndDateRange(member.getId(), startDate, today);
        int remainingBudget = member.getMonthlyBudget() - totalSpent;
        int dailySafeAmount = remainingBudget / calculationDDay;

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

        // 3. 기획 가이드라인이 반영된 캐릭터 상태 및 메시지 결정
        String type = "FLEX";
        String message = "오케이! 그럼 하루에 " + String.format("%,d", dailySafeAmount) + "원 소비하면 적절해요!";

        // 조건 1: 3일 이내에 다가오는 정기결제가 있는 경우 우선 알림
        if (upcomingDto != null && upcomingDto.getDDayToBilling() <= 3 && upcomingDto.getDDayToBilling() >= 0) {
            type = "WARNING";
            message = String.format("%d일 뒤에 %s (%s원) 결제가 있어!",
                    upcomingDto.getDDayToBilling(),
                    upcomingDto.getPaymentName(),
                    String.format("%,d", upcomingDto.getAmount()));
        }
        // 조건 2: 하루 권장 소비액이 5천 원 미만인 경우 (위험/파산 단계)
        else if (dailySafeAmount < 5000) {
            type = "EMPTY_ACCOUNT";
            int deficit = Math.abs(remainingBudget);
            int gimbapCount = Math.max(2, deficit / 1300); // 삼각김밥 1개 1,300원 기준

            message = String.format("36,000원 더 쓰면 하루를 삼각김밥 %d개(1,300원)로 버텨야 해ㅜ", gimbapCount);
        }
        // 조건 3: 하루 권장 소비액이 1만 5천 원 미만인 경우 (주의 단계)
        else if (dailySafeAmount < 15000) {
            type = "WARNING";
            message = "이젠 삼각김밥을 자주 먹는게 좋겠어요.. ㅎㅎ";
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