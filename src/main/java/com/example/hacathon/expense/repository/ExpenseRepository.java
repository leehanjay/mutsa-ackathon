package com.example.hacathon.expense.repository;

import com.example.hacathon.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.member.id = :memberId AND e.expenseDate >= :startDate AND e.expenseDate <= :endDate")
    Integer sumAmountByMemberIdAndDateRange(@Param("memberId") Long memberId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //소비내역 최신순으로 조회
    List<Expense> findAllByMemberIdAndExpenseDateBetweenOrderByExpenseDateDescIdDesc(Long memberId, LocalDate startDate, LocalDate endDate);
}