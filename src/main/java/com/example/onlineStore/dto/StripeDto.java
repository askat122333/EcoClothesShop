package com.example.onlineStore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeDto {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String description;
    private String receiptEmail;
}
