package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class PayPalDto {
    private Long id;

    private String user;

    private List<Product> products;
    private String address;

    private Double sum;

}
