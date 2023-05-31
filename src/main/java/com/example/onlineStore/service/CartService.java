package com.example.onlineStore.service;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CartService {
    CartDto getById(Long id) throws CartNotFoundException;

    Cart getByIdEntity(Long id);

    List<CartDto> getAll() throws CartNotFoundException;

    CartDto create(CartDto dto);

    CartDto update(Long id,CartDto dto) throws CartNotFoundException;

    String deleteById(Long id) throws CartNotFoundException;
     CartDto addNewProduct(Long userId,Long productId) throws UserNotFoundException, CartNotFoundException;
    CartDto removeProduct(Long userId, Long productId) throws CartNotFoundException;
    CartDto findByUser(Long id);
}
