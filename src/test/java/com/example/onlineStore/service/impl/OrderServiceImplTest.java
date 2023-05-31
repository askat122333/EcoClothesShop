package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.Gender;
import com.example.onlineStore.enums.Roles;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private OrderServiceImpl orderServices;
/*
    @Test
    public void create() {
        User user = User.builder()
                .id(1l)
                .name("name")
                .surname("surname")
                .email("email")
                .password("password")
                .
        (1l,"name","surname","email","password",null, Roles.USER,
                Gender.UNKNOWN,null);
        Product product = new Product(1l,"product",123d,null, Size.XL,"material",
                null,null );
        Cart cart = new Cart(null,user,null,product,null);
        Order order = new Order(null,cart,null);
        OrderDto orderDto = orderServices.mapToDto(order);
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(cartRepository.findByUserAndRdtIsNull(user)).thenReturn(cart);
        Mockito.when(userRepository.findByIdAndRdtIsNull(user.getId())).thenReturn(user);
        assertEquals(orderDto.getUser(),orderServices.create(user.getId()).getUser());

    }*/
}