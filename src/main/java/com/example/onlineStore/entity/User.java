package com.example.onlineStore.entity;

import com.example.onlineStore.enums.Gender;
import com.example.onlineStore.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "user_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Не может быть пустым.")
    private String name;
    @NotBlank(message = "Не может быть пустым.")
    private String surname;
    @Email(message = "Не соответствует формату email.")
    private String email;
    @NotBlank(message = "Не может быть пустым.")
    private String password;
    private byte[] photo;
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Size(min = 9, max = 10, message = "Должно содержать 9 или 10 символов.")
    private String phone;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> order;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private PaymentCard paymentCard;
    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    private String token;
    private LocalDateTime tokenExpiry;

    private LocalDate rdt;

}
