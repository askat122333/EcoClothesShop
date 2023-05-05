package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private OrderDto mapToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUser(),
                order.getCart());
    }

    @Override
    public OrderDto getById(Long id) {
        Optional<Order> order = Optional.of(orderRepository.findById(id).orElse(new Order()));
        return mapToDto(order.get());
    }

    @Override
    public Order getByIdEntity(Long id) {
        Order order = orderRepository.findById(id).get();
        return order;
    }

    @Override
    public List<OrderDto> getAll() {
        List<Order> orderList = orderRepository.findAll();
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order:orderList) {
            orderDtoList.add(mapToDto(order));
        }
        return orderDtoList;
    }

    @Override
    public OrderDto create(Order order) {
        return mapToDto(orderRepository.save(order));
    }

    @Override
    public OrderDto update(Order order) {
        return mapToDto(orderRepository.save(order));
    }

    @Override
    public String deleteById(Long id) {
        return null;
    }
}
