package com.example.onlineStore.entity;

import com.example.onlineStore.enums.CartStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
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
    @ManyToMany
    private List<Product> products;
    @Enumerated(value = EnumType.STRING)
    private CartStatus cartStatus;

    private LocalDate rdt;
}
