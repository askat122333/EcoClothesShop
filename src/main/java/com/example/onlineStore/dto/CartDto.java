package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private User user;
    private Order order;

    private Product product;
}
