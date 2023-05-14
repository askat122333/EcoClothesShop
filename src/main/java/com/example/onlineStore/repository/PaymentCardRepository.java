package com.example.onlineStore.repository;

import com.example.onlineStore.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard,Long> {
    PaymentCard findByIdAndRdtIsNull(Long id);

    List<PaymentCard> findAllByRdtIsNull();
}
