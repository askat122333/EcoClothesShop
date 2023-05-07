package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import lombok.*;

import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartDto {
    private Long id;
    private User user;
    private Order order;

    private Product product;

}
