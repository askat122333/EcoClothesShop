package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Payment;
import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.enums.Gender;
import com.example.onlineStore.enums.Roles;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private Roles role;

    private Gender gender;
    private String phone;

    private PaymentCard paymentCard;


}
