package com.example.onlineStore.service;

import com.example.onlineStore.dto.CreateStripeCustomerDto;
import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.dto.StripeDto;
import com.example.onlineStore.entity.Payment;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.PaymentNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentService {
    PaymentDto getById(Long id) throws PaymentNotFoundException;

    Payment getByIdEntity(Long id);

    List<PaymentDto> getAll() throws PaymentNotFoundException;

    PaymentDto create(PaymentDto dto);

    PaymentDto update(Long id,PaymentDto dto) throws PaymentNotFoundException;

    String deleteById(Long id) throws PaymentNotFoundException;
    String makePayment(Long orderId) throws OrderNotFoundException, UserNotFoundException, PaymentCardNotFoundException;
    String stripePayment(Long userId , StripeDto dto) throws StripeException, OrderNotFoundException, UserNotFoundException;
    public String addCustomerAndCard(CreateStripeCustomerDto dto) throws StripeException;

}
