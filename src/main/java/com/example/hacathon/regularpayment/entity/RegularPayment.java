package com.example.hacathon.regularpayment.entity;

import com.example.hacathon.basetimeentity.BaseTimeEntity;
import com.example.hacathon.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "regular_payment")
public class RegularPayment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "payment_name", nullable = false, length = 255)
    private String paymentName;

    @Column(nullable = false)
    private Integer amount;

    @Column(name = "billing_day", nullable = false)
    private Integer billingDay;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Builder
    public RegularPayment(Member member, String paymentName, Integer amount, Integer billingDay, Boolean isActive) {
        this.member = member;
        this.paymentName = paymentName;
        this.amount = amount;
        this.billingDay = billingDay;
        this.isActive = isActive != null ? isActive : true;
    }
    public static RegularPayment createNew(Member member, String paymentName, Integer amount, Integer billingDay) {
        if (member == null) {
            throw new IllegalArgumentException("회원 정보는 필수입니다.");
        }
        if (paymentName == null || paymentName.isBlank()) {
            throw new IllegalArgumentException("구독 서비스명은 필수 입력값입니다.");
        }
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("구독 금액은 0원 이상이어야 합니다.");
        }
        if (billingDay == null || billingDay < 1 || billingDay > 31) {
            throw new IllegalArgumentException("결제일은 1일에서 31일 사이여야 합니다.");
        }

        return RegularPayment.builder()
                .member(member)
                .paymentName(paymentName)
                .amount(amount)
                .billingDay(billingDay)
                .isActive(true)
                .build();
    }
    // 정기결제 상태 토글 (true <-> false)
    public void toggleActive() {
        this.isActive = !this.isActive;
    }
}
