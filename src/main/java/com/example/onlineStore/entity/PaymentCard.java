package com.example.onlineStore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payment_card")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "card_num")
    @Length(min = 12,max = 12,message = "Должно содержать 12 цифр.")
    private String cardNum;
    @Min(value = 1,message = "Должно быть положительным.")
    private Double balance;
    private LocalDate rdt;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
