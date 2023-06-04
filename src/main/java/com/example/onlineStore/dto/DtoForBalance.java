package com.example.onlineStore.dto;

import com.example.onlineStore.entity.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoForBalance {
    private String cardNum;
    private Double balance;
    private Long user;
}
