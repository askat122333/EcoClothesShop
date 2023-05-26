package com.example.onlineStore.dto;

import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.enums.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private Double price;

    private byte[] image;
    private Size size;

    private String material;

    private String img;

    private ProductType productType;

    private LocalDate dateAdded;

    private Discount discount;

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
