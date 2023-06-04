package com.example.onlineStore.repository;

import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard,Long> {
    PaymentCard findByIdAndRdtIsNull(Long id);
    PaymentCard findByUserAndRdtIsNull(User user);

    List<PaymentCard> findAllByRdtIsNull();
    @Query(value = "select * from payment_card where user_id = ?", nativeQuery = true)
    PaymentCard findByUserAndRdtIsNull(Long userId);
}
