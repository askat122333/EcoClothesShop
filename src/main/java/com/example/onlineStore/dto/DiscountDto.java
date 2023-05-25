package com.example.onlineStore.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountDto {
    private Long id;
    private String name;
    private Double discount;
    private LocalDate rdt;
}
