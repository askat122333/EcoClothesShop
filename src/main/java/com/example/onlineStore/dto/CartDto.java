package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartDto {

    private Long id;

    private User user;
    private Double sum;

    private List<Product> products;

    private LocalDate rdt;

}
