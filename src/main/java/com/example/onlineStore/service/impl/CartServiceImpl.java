package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private CartDto mapToDto(Cart cart){
        return new CartDto(
          cart.getId(),
          cart.getUser(),
          cart.getOrder(),
          cart.getProduct()
        );
    }

    @Override
    public CartDto getById(Long id) {
        Optional<Cart> cart = Optional.of(cartRepository.findById(id).orElse(new Cart()));
        return mapToDto(cart.get());
    }

    @Override
    public Cart getByIdEntity(Long id) {
        Cart cart = cartRepository.findById(id).get();
        return cart;
    }

    @Override
    public List<CartDto> getAll() {
        List<Cart> cartList = cartRepository.findAll();
        List<CartDto> cartDtoList = new ArrayList<>();
        for (Cart cart:cartList) {
            cartDtoList.add(mapToDto(cart));
        }
        return cartDtoList;
    }

    @Override
    public CartDto create(Cart cart) {
        return mapToDto(cartRepository.save(cart));
    }

    @Override
    public CartDto update(Cart cart) {
        return mapToDto(cartRepository.save(cart));
    }

    @Override
    public String deleteById(Long id) {
        return null;
    }
}
