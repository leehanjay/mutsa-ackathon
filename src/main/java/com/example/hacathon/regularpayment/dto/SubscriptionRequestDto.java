package com.example.hacathon.regularpayment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionRequestDto {
    private String paymentName;
    private Integer amount;
    private Integer billingDay;
}