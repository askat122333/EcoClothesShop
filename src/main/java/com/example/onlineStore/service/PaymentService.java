package com.example.onlineStore.service;

import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentService {
    PaymentDto getById(Long id);

    Payment getByIdEntity(Long id);

    List<PaymentDto> getAll();

    PaymentDto create(Payment payment);

    PaymentDto update(Long id,PaymentDto dto);

    String deleteById(Long id);

}
