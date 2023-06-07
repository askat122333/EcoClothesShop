package com.example.onlineStore.service;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.dto.PayPalDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {
    OrderDto getById(Long id) throws OrderNotFoundException;

    Order getByIdEntity(Long id);

    List<OrderDto> getAll() throws OrderNotFoundException;


    OrderDto create(Long userId, String address) throws CartNotFoundException;

    OrderDto update(Long id, OrderDto dto) throws OrderNotFoundException;

    String deleteById(Long id) throws OrderNotFoundException;
    OrderDto quickCreate(Long userId, Long productId,String address) throws ProductNotFoundException;
    PayPalDto findByUserId(Long id);
}
