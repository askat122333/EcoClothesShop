package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartDto mapToDto(Cart cart){
        return new CartDto(
                cart.getId(),
                cart.getUser(),
                cart.getSum(),
                cart.getProducts(),
                cart.getRdt()
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
        if(cart.getSum()!=null){
            cart.setSum(dto.getSum());
        }
        if(dto.getProducts()!=null){
            cart.setProducts(dto.getProducts());
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


    @Override
    public CartDto addNewProduct(Long userId,Long productId) {
        User user  = userRepository.findByIdAndRdtIsNull(userId);
       Cart cart = cartRepository.findByUserAndRdtIsNull(user);
       Product product = productRepository.findByIdAndRdtIsNull(productId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setProducts(List.of(product));
            newCart.setSum(getSum(List.of(product)));
            cartRepository.save(newCart);
            return mapToDto(newCart);
        }else
            cart.getProducts().add(product);
        cart.setSum(getSum(cart.getProducts()));
        cartRepository.save(cart);
        return mapToDto(cart);
    }
    private Double getSum(List<Product> products){
        Double sum = 0d;
        for (int i = 0; i <products.size() ; i++) {
            sum += products.get(i).getPrice();
        }
        return sum;
    }

    @Override
    public CartDto removeProduct(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserAndRdtIsNull(userRepository.findByIdAndRdtIsNull(userId));
       cart.getProducts().removeIf(product -> product.getId() == productId);
       cart.setSum(getSum(cart.getProducts()));
        cartRepository.save(cart);
        return mapToDto(cart);
    }
}
