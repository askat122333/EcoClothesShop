package com.example.onlineStore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @ManyToOne
    private User user;
    @OneToMany
    private List<Product> products;
    private String address;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "payment_id",referencedColumnName = "id")
    private Payment payment;
    private Double sum;

    @Column(name = "order_time")
    private LocalDate orderTime;

    private LocalDate rdt;

}
