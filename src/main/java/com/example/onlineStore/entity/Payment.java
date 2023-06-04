package com.example.onlineStore.entity;

import com.example.onlineStore.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Min(value = 1,message = "Должно быть положительным.")
    private Double sum;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(name = "payment_time")
    private LocalDate paymentTime;
    @Column(name = "card_num")
    private String cardNum;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    private String receipt;
    private LocalDate rdt;
}
