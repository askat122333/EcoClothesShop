package com.example.onlineStore.service;

import com.example.onlineStore.dto.PaymentCardDto;
import com.example.onlineStore.entity.PaymentCard;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentCardService {
    PaymentCardDto getById(Long id);

    PaymentCard getByIdEntity(Long id);

    List<PaymentCardDto> getAll();

    PaymentCardDto create(PaymentCard paymentCard);

    PaymentCardDto update(Long id,PaymentCardDto dto);

    String deleteById(Long id);
}
