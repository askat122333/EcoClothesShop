package com.example.onlineStore.entity;

import com.example.onlineStore.enums.Gender;
import com.example.onlineStore.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "user_table")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Byte[] photo;
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phone;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> order;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "card_id",referencedColumnName = "id")
    private PaymentCard paymentCard;
    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    private LocalDate rdt;

}
