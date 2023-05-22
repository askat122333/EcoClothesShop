package com.example.onlineStore.dto;

import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Long id;
    private Double sum;

    private PaymentStatus status;

    private LocalDate paymentTime;

    private String cardNum;

    private User user;
    private LocalDate rdt;
}
