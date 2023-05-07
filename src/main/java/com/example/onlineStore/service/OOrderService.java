package com.example.onlineStore.service;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OOrderService {
    OrderDto getById(Long id);

    Order getByIdEntity(Long id);

    List<OrderDto> getAll();
//TODO
    OrderDto create(Long userId);

    OrderDto update(Long id,OrderDto dto);

    String deleteById(Long id);

}
