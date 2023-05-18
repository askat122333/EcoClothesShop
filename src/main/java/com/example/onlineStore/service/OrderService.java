package com.example.onlineStore.service;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {
    OrderDto getById(Long id);

    Order getByIdEntity(Long id);

    List<OrderDto> getAll();


    OrderDto create(Long userId, String address);

    OrderDto update(Long id, OrderDto dto);

    String deleteById(Long id);
    OrderDto quickCreate(Long userId, Long productId,String address);
}
