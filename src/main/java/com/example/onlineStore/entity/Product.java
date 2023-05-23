package com.example.onlineStore.entity;

import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.enums.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price;
    @Enumerated(EnumType.STRING)
    private Size size;
    private String material;
    private byte[] image;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    @Column(name = "date_added")
    private LocalDate dateAdded;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "discount_id",referencedColumnName = "id")
    private Discounts discount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private LocalDate rdt;
}
