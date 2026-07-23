package com.example.hacathon.regularpayment.repository;

import com.example.hacathon.regularpayment.entity.RegularPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegularPaymentRepository extends JpaRepository<RegularPayment, Long> {
    List<RegularPayment> findAllByMemberIdAndIsActiveTrue(Long memberId);
}