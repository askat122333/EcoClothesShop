package com.example.onlineStore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "discount")
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double discount;
    private LocalDate rdt;
}
