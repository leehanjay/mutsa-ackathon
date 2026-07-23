package com.example.hacathon.expense.entity;

import com.example.hacathon.basetimeentity.BaseTimeEntity;
import com.example.hacathon.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "expense")
public class Expense extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private Integer amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(length = 255)
    private String memo;

    @Builder
    public Expense(Member member, String category, Integer amount, LocalDate expenseDate, String memo) {
        this.member = member;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.memo = memo;
    }

    public static Expense createNew(Member member, String category, Integer amount, LocalDate expenseDate, String memo) {
        if (member == null) {
            throw new IllegalArgumentException("회원 정보는 필수입니다.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("소비 카테고리는 필수 입력값입니다.");
        }
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("소비 금액은 0원 이상이어야 합니다.");
        }
        if (expenseDate == null) {
            throw new IllegalArgumentException("소비 날짜는 필수 입력값입니다.");
        }

        return Expense.builder()
                .member(member)
                .category(category)
                .amount(amount)
                .expenseDate(expenseDate)
                .memo(memo)
                .build();
    }
}
