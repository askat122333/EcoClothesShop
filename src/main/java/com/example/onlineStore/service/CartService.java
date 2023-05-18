package com.example.onlineStore.service;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CartService {
    CartDto getById(Long id);

    Cart getByIdEntity(Long id);

    List<CartDto> getAll();

    CartDto create(Cart cart);

    CartDto update(Long id,CartDto dto);

    String deleteById(Long id);
     CartDto addNewProduct(Long userId,Long productId);
    CartDto removeProduct(Long userId, Long productId);
}
