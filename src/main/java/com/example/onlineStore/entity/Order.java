package com.example.onlineStore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToMany
    private List<Product> products;
    @NotBlank(message = "Не должно быть пустым.")
    private String address;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "payment_id",referencedColumnName = "id")
    private Payment payment;
    @Min(value = 1,message = "Должно быть положительным.")
    private Double sum;

    @Column(name = "order_time")
    private LocalDate orderTime;

    private LocalDate rdt;

}
