package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Category;
import com.example.onlineStore.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Double price;

    private byte[] image;
    private Size size;

    private String material;

    private String img;

    private Category category;
    private LocalDate rdt;

    public ProductDto(Long id, String name, Double price,  Size size, String material, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;

        this.size = size;
        this.material = material;
        this.category = category;
    }
}
