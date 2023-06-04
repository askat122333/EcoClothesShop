package com.example.onlineStore.entity;

import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.enums.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Не может быть пустым.")
    private String name;
    @Min(value = 1, message = "Должно быть положительным.")
    private Double price;
    @Enumerated(EnumType.STRING)
    private Size size;
    @NotBlank(message = "Не может быть пустым.")
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
    private Discount discount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @ManyToMany
    private List<Cart> cart;

    private LocalDate rdt;
}
