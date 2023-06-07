package com.example.onlineStore.dto;

import com.example.onlineStore.enums.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto {
    private String material;
    private String name;
    private Double price;
    private Size size;
    private Long categoryId;
    private Long discountId;
}
