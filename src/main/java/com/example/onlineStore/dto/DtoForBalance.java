package com.example.onlineStore.dto;

import lombok.*;



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
