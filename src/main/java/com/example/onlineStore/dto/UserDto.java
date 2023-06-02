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
    private String login;
    private String email;
    private String password;
    private byte[] photo;

    private Roles role;

    private Gender gender;
    private String phone;

    private PaymentCard paymentCard;

    private List<Payment> payments;

    private LocalDate rdt;

    public UserDto(Long id, String name, String login, String email, String password, byte[] photo, Roles role, Gender gender) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.role = role;
        this.gender = gender;
    }
}
