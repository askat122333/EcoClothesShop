package com.example.onlineStore.service;

import com.example.onlineStore.dto.PaymentCardDto;
import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentCardService {
    PaymentCardDto getById(Long id) throws PaymentCardNotFoundException;

    PaymentCard getByIdEntity(Long id);

    List<PaymentCardDto> getAll() throws PaymentCardNotFoundException;

    PaymentCardDto create(PaymentCardDto dto);

    PaymentCardDto update(Long id,PaymentCardDto dto) throws PaymentCardNotFoundException;

    String deleteById(Long id) throws PaymentCardNotFoundException;
}
