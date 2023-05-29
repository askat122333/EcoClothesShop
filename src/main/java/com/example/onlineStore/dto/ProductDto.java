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

    private Size size;

    private String material;

    private ProductType productType;

    private LocalDate dateAdded;

    private Discount discount;

    private Category category;
    private LocalDate rdt;

}
