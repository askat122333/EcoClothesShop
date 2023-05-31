package com.example.onlineStore.dto;

import lombok.Data;

@Data
public class CreateStripeCustomerDto {
    private String name;
    private String email;
    private String cardNumber;
    private String exp_month;
    private String exp_year;
    private String cvc;

}
