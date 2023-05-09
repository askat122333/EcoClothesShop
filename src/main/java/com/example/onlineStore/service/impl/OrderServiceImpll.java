package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.OOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpll implements OOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    public OrderDto mapToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getCart(),
                order.getOrderTime(),
                order.getRdt()
        );
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
//TODO
    @Override
    public OrderDto create(Long userId) {
        Order order = new Order();
        order.setCart(cartRepository.findByUserAndRdtIsNull(userRepository.findByIdAndRdtIsNull(userId)));
        order.setOrderTime(LocalDate.now());
        return mapToDto(orderRepository.save(order));
    }

    @Override
    public OrderDto update(Long id,OrderDto dto) {
        Order order = getByIdEntity(id);
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
