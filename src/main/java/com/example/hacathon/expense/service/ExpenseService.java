package com.example.hacathon.expense.service;

import com.example.hacathon.expense.dto.ExpenseRequestDto;
import com.example.hacathon.expense.dto.ExpenseResponseDto;
import com.example.hacathon.expense.entity.Expense;
import com.example.hacathon.expense.repository.ExpenseRepository;
import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ExpenseResponseDto createExpense(Long memberId, ExpenseRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        LocalDate today = LocalDate.now(); // 프론트 요청대로 서버에서 날짜 채번

        // 1. 소비 내역 저장 (merchant를 기존 category에 매핑, 빈 메모 전달)
        Expense expense = Expense.createNew(
                member,
                request.getMerchant(),
                request.getAmount(),
                today,
                ""
        );
        Expense savedExpense = expenseRepository.save(expense);

        // 2. 이번 달 누적 지출액(spentAmount) 계산 (월급일 기준)
        int currentDay = today.getDayOfMonth();
        int payDay = member.getPayDay() != null ? member.getPayDay() : 1;
        LocalDate startDate;

        if (payDay > currentDay) {
            startDate = today.minusMonths(1).withDayOfMonth(payDay);
        } else {
            startDate = today.withDayOfMonth(payDay);
        }

        Integer totalSpent = expenseRepository.sumAmountByMemberIdAndDateRange(member.getId(), startDate, today);

        // 3. 프론트엔드가 요구한 스펙에 맞춰 응답 DTO 조립
        return ExpenseResponseDto.builder()
                .id("exp-" + savedExpense.getId()) // "exp-123" 형태로 가공
                .merchant(savedExpense.getCategory())
                .amount(savedExpense.getAmount())
                .date(savedExpense.getExpenseDate())
                .spentAmount(totalSpent)
                .build();
    }
}