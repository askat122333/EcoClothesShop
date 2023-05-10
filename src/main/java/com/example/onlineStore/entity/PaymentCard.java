package com.example.onlineStore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payment_card")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "card_num")
    private Integer cardNum;
    private Double balance;
    private LocalDate rdt;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
