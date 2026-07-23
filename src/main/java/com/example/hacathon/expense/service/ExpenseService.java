package com.example.hacathon.expense.service;

import com.example.hacathon.expense.dto.ExpenseListResponseDto;
import com.example.hacathon.expense.dto.ExpenseRequestDto;
import com.example.hacathon.expense.dto.ExpenseResponseDto;
import com.example.hacathon.expense.entity.Expense;
import com.example.hacathon.expense.repository.ExpenseRepository;
import com.example.hacathon.global.apiPayload.code.GeneralErrorCode;
import com.example.hacathon.global.apiPayload.exception.ProjectException;
import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ExpenseResponseDto createExpense(Long memberId, ExpenseRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ProjectException(GeneralErrorCode.MEMBER_NOT_FOUND));

        LocalDate today = LocalDate.now();

        Expense expense = Expense.createNew(
                member,
                request.getMerchant(),
                request.getAmount(),
                today,
                ""
        );
        Expense savedExpense = expenseRepository.save(expense);

        int currentDay = today.getDayOfMonth();
        int payDay = member.getPayDay() != null ? member.getPayDay() : 1;
        LocalDate startDate;

        if (payDay > currentDay) {
            startDate = today.minusMonths(1).withDayOfMonth(payDay);
        } else {
            startDate = today.withDayOfMonth(payDay);
        }

        Integer totalSpent = expenseRepository.sumAmountByMemberIdAndDateRange(member.getId(), startDate, today);

        return ExpenseResponseDto.builder()
                .id("exp-" + savedExpense.getId())
                .merchant(savedExpense.getCategory())
                .amount(savedExpense.getAmount())
                .date(savedExpense.getExpenseDate())
                .spentAmount(totalSpent)
                .build();
    }

    @Transactional(readOnly = true)
    public ExpenseListResponseDto getExpenseList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ProjectException(GeneralErrorCode.MEMBER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();
        int payDay = member.getPayDay() != null ? member.getPayDay() : 1;
        LocalDate startDate;

        if (payDay > currentDay) {
            startDate = today.minusMonths(1).withDayOfMonth(payDay);
        } else {
            startDate = today.withDayOfMonth(payDay);
        }

        // 총 지출액 계산
        Integer totalSpent = expenseRepository.sumAmountByMemberIdAndDateRange(member.getId(), startDate, today);

        // 기간 내 소비 리스트 조회
        List<Expense> expenseList = expenseRepository.findAllByMemberIdAndExpenseDateBetweenOrderByExpenseDateDescIdDesc(member.getId(), startDate, today);

        List<ExpenseListResponseDto.ExpenseItemDto> itemDtos = expenseList.stream()
                .map(e -> ExpenseListResponseDto.ExpenseItemDto.builder()
                        .id(e.getId())
                        .merchant(e.getCategory())
                        .amount(e.getAmount())
                        .date(e.getExpenseDate())
                        .build())
                .collect(Collectors.toList());

        return ExpenseListResponseDto.builder()
                .totalSpentAmount(totalSpent)
                .expenses(itemDtos)
                .build();
    }

    @Transactional
    public void deleteExpense(Long memberId, Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ProjectException(GeneralErrorCode._BAD_REQUEST));

        // 본인 내역이 맞는지 확인 (보안)
        if (!expense.getMember().getId().equals(memberId)) {
            throw new ProjectException(GeneralErrorCode._FORBIDDEN);
        }

        expenseRepository.delete(expense);
    }
}