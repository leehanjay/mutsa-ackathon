package com.example.hacathon.home.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeResponseDto {
    private String nickname;
    private Integer payDay;
    private Integer dDayToPayDay;
    private Integer monthlyBudget;
    private Integer totalSpentThisMonth;
    private Integer remainingBudget;
    private Integer dailySafeAmount;
    private String characterType;
    private String characterMessage;
    private UpcomingSubscriptionDto upcomingSubscription;

    @Getter
    @Builder
    public static class UpcomingSubscriptionDto {
        private Long paymentId;
        private String paymentName;
        private Integer amount;
        private Integer billingDay;
        private Integer dDayToBilling;
    }
}