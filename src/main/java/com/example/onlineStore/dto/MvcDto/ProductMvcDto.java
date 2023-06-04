package com.example.onlineStore.dto.MvcDto;

import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.enums.Size;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMvcDto {

    private Long id;
    private String name;
    private Double price;
    private byte[] image;
    private Size size;

    private String material;

    private ProductType productType;

    private LocalDate dateAdded;
}
