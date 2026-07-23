package com.example.hacathon.expense.service;

import com.example.hacathon.expense.dto.ExpenseRequestDto;
import com.example.hacathon.expense.entity.Expense;
import com.example.hacathon.expense.repository.ExpenseRepository;
import com.example.hacathon.member.entity.Member;
import com.example.hacathon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createExpense(Long memberId, ExpenseRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 💡 수정됨: 엔티티에 만들어두신 강력한 예외처리 메서드(createNew)를 사용하여 안전하게 생성합니다!
        Expense expense = Expense.createNew(
                member,
                request.getCategory(),
                request.getAmount(),
                request.getExpenseDate(),
                request.getMemo()
        );

        Expense savedExpense = expenseRepository.save(expense);
        return savedExpense.getId();
    }
}