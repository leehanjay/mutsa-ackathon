package com.example.hacathon.home.service;

import com.example.hacathon.expense.repository.ExpenseRepository;
import com.example.hacathon.global.apiPayload.code.GeneralErrorCode;
import com.example.hacathon.global.apiPayload.exception.ProjectException;
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
                .orElseThrow(() ->
                        new ProjectException(GeneralErrorCode.MEMBER_NOT_FOUND)
                );

        // 온보딩에서 예산과 월급일을 설정하지 않은 경우
        if (member.getPayDay() == null ||
                member.getMonthlyBudget() == null) {
            throw new ProjectException(
                    GeneralErrorCode._BAD_REQUEST
            );
        }

        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();
        int payDay = member.getPayDay();

        int dDayToPayDay;
        LocalDate startDate;

        if (payDay > currentDay) {
            dDayToPayDay = payDay - currentDay;
            startDate = today.minusMonths(1)
                    .withDayOfMonth(payDay);
        } else if (payDay < currentDay) {
            dDayToPayDay =
                    today.lengthOfMonth() - currentDay + payDay;
            startDate = today.withDayOfMonth(payDay);
        } else {
            dDayToPayDay = 0;
            startDate = today.withDayOfMonth(payDay);
        }

        int calculationDDay =
                dDayToPayDay == 0 ? 1 : dDayToPayDay;

        int totalSpent =
                expenseRepository.sumAmountByMemberIdAndDateRange(
                        member.getId(),
                        startDate,
                        today
                );

        int remainingBudget =
                member.getMonthlyBudget() - totalSpent;

        int dailySafeAmount =
                remainingBudget / calculationDDay;

        List<RegularPayment> subscriptions =
                regularPaymentRepository
                        .findAllByMemberIdAndIsActiveTrue(
                                member.getId()
                        );

        HomeResponseDto.UpcomingSubscriptionDto upcomingDto = null;
        int minSubDday = Integer.MAX_VALUE;

        for (RegularPayment sub : subscriptions) {
            int subDay = sub.getBillingDay();

            int subDday = subDay >= currentDay
                    ? subDay - currentDay
                    : today.lengthOfMonth()
                    - currentDay
                    + subDay;

            if (subDday < minSubDday) {
                minSubDday = subDday;

                upcomingDto =
                        HomeResponseDto
                                .UpcomingSubscriptionDto
                                .builder()
                                .paymentId(sub.getId())
                                .paymentName(sub.getPaymentName())
                                .amount(sub.getAmount())
                                .billingDay(subDay)
                                .dDayToBilling(subDday)
                                .build();
            }
        }

        String type = "FLEX";

        String message =
                "오케이! 그럼 하루에 "
                        + String.format("%,d", dailySafeAmount)
                        + "원 소비하면 적절해요!";

        if (upcomingDto != null
                && upcomingDto.getDDayToBilling() <= 3
                && upcomingDto.getDDayToBilling() >= 0) {

            type = "WARNING";

            message = String.format(
                    "%d일 뒤에 %s (%s원) 결제가 있어!",
                    upcomingDto.getDDayToBilling(),
                    upcomingDto.getPaymentName(),
                    String.format("%,d", upcomingDto.getAmount())
            );

        } else if (dailySafeAmount < 5000) {
            type = "EMPTY_ACCOUNT";

            int deficit = Math.abs(remainingBudget);
            int gimbapCount = Math.max(2, deficit / 1300);

            message = String.format(
                    "36,000원 더 쓰면 하루를 삼각김밥 %d개(1,300원)로 버텨야 해ㅜ",
                    gimbapCount
            );

        } else if (dailySafeAmount < 15000) {
            type = "WARNING";
            message = "이젠 삼각김밥을 자주 먹는 게 좋겠어요.. ㅎㅎ";
        }

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