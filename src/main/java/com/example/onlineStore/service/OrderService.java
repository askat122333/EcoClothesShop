package com.example.onlineStore.service;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {
    OrderDto getById(Long id);

    Order getByIdEntity(Long id);

    List<OrderDto> getAll();

    OrderDto create(Order order);

    OrderDto update(Order order);

    String deleteById(Long id);

}
