package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Category;
import com.example.onlineStore.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private Byte[] photo;

    private Size size;
    private String material;

    private Category category;
    private LocalDate rdt;

}
