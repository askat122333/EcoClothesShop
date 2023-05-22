package com.example.onlineStore.dto;

import com.example.onlineStore.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCardDto {

    private Long id;

    private String cardNum;
    private Double balance;
    private LocalDate rdt;

    private User user;
}
