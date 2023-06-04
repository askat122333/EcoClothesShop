package com.example.onlineStore.service;

import com.example.onlineStore.dto.DtoForBalance;
import com.example.onlineStore.dto.PaymentCardDto;
import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentCardService {
    PaymentCardDto getById(Long id) throws PaymentCardNotFoundException;
    DtoForBalance getUserBalance(Long userId) throws PaymentCardNotFoundException;

    PaymentCard getByIdEntity(Long id);

    List<PaymentCardDto> getAll() throws PaymentCardNotFoundException;

    String create(PaymentCardDto dto,Long userId)throws UserNotFoundException;

    PaymentCardDto update(Long id,PaymentCardDto dto) throws PaymentCardNotFoundException;

    String deleteById(Long id) throws PaymentCardNotFoundException;
}
