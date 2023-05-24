package com.example.onlineStore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto {
    private Long id;
    private String name;
    private Double discount;
    private LocalDate rdt;
}
