package com.example.onlineStore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Min(value = 1,message = "Должно быть положительным.")
    private Double sum;
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private List<Product> products;

    private LocalDate rdt;
}
