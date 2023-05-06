package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.service.CartService;
import com.example.onlineStore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductServiceImpl productService;
    private final UserService userService;
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
        Cart cart = cartRepository.findByIdAndRdtIsNull(id);
        return mapToDto(cart);
    }

    @Override
    public Cart getByIdEntity(Long id) {
        return cartRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<CartDto> getAll() {
        List<Cart> cartList = cartRepository.findAllByRdtIsNull();
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
    public CartDto update(Long id,CartDto dto) {
        Cart cart = getByIdEntity(id);
        if(dto.getUser()!=null){
            cart.setUser(dto.getUser());
        }
        if(dto.getOrder()!=null){
            cart.setOrder(dto.getOrder());
        }
        if(dto.getProduct()!=null){
            cart.setProduct(dto.getProduct());
        }
        return mapToDto(cartRepository.save(cart));
    }

    @Override
    public String deleteById(Long id) {
        Cart cart = getByIdEntity(id);
        cart.setRdt(LocalDate.now());
        cartRepository.save(cart);
        return "Корзина с id: "+id+" была удалена.";
    }

    //TODO
    @Override
    public CartDto addNewProduct(Long userId,Long productId) {
        Cart cart = new Cart();
        cart.setUser(userService.getByIdEntity(userId));
        cart.setProduct(productService.getByIdEntity(productId));
        cartRepository.save(cart);
        return mapToDto(cart);
    }
    //TODO
    @Override
    public CartDto removeProduct(Long cartId, Long productId) {
        Cart cart = cartRepository.findByIdAndProductAndRdtIsNull(cartId,productId);
        cart.setProduct(null);
        cartRepository.save(cart);
        return mapToDto(cart);
    }
}
