package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.*;
import com.example.onlineStore.enums.PaymentStatus;
import com.example.onlineStore.repository.*;
import com.example.onlineStore.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    public OrderDto mapToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUser(),
                order.getProducts(),
                order.getAddress(),
                order.getPayment(),
                order.getSum(),
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

    @Override
    public OrderDto create(Long userId, String address) {
        Cart cart = cartRepository.findByUserAndRdtIsNull(userRepository.findByIdAndRdtIsNull(userId));
        Order order = new Order();
        order.setUser(userRepository.findByIdAndRdtIsNull(userId));
        List<Product> products = new ArrayList<>();
        products.addAll(cart.getProducts());
        order.setProducts(products);
        order.setAddress(address);
        order.setSum(cart.getSum());
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        order.setPayment(payment);
        order.setOrderTime(LocalDate.now());
        orderRepository.save(order);
        return mapToDto(order);
    }

    @Override
    public OrderDto update(Long id,OrderDto dto) {
        Order order = getByIdEntity(id);
        if(dto.getUser()!=null){
            order.setUser(dto.getUser());
        }
        if(dto.getProducts()!=null){
            order.setProducts(dto.getProducts());
        }
        if(dto.getAddress()!=null){
            order.setAddress(dto.getAddress());
        }
        if(dto.getPayment()!=null){
            order.setPayment(dto.getPayment());
        }
        if(dto.getSum()!=null){
            order.setSum(dto.getSum());
        }
        if(dto.getOrderTime()!=null){
            order.setOrderTime(dto.getOrderTime());
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

    @Override
    public OrderDto quickCreate(Long userId, Long productId, String address) {
        Order order = new Order();
        order.setUser(userRepository.findByIdAndRdtIsNull(userId));
        Product product = productRepository.findByIdAndRdtIsNull(productId);
        order.setProducts(List.of(product));
        order.setAddress(address);
        order.setSum(product.getPrice());
        order.setOrderTime(LocalDate.now());
        orderRepository.save(order);
        return mapToDto(order);
    }
}
