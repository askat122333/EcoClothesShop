package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        Order order = orderRepository.findByIdAndRdtIsNull(id);
        return mapToDto(order);
    }

    @Override
    public Order getByIdEntity(Long id) {
        return orderRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<OrderDto> getAll() {
        List<Order> orderList = orderRepository.findAllByRdtIsNull();
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
    public OrderDto update(Long id,OrderDto dto) {
        Order order = getByIdEntity(id);
        if(dto.getUser()!=null){
            order.setUser(dto.getUser());
        }
        if(dto.getCart()!=null){
            order.setCart(dto.getCart());
        }
        return mapToDto(orderRepository.save(order));
    }

    @Override
    public String deleteById(Long id) {
        Order order = getByIdEntity(id);
        order.setRdt(LocalDate.now());
        orderRepository.save(order);
        return "Заказ с id: "+id+" был удален.";
    }
}
