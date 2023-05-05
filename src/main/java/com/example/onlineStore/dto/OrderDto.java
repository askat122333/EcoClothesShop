package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Cart;
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
public class OrderDto {
    private Long id;
    private User user;
    private Cart cart;

}
