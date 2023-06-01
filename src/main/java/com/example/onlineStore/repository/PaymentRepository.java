package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByIdAndRdtIsNull(Long id);

    List<Payment> findAllByRdtIsNull();


}
