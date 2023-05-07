package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.Gender;
import com.example.onlineStore.enums.Roles;
import com.example.onlineStore.enums.Size;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {
    @Mock
    private  CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CartServiceImpl cartService;

    @Test
  public   void addNewProduct() {
        User user = new User(1l,"name","surname","email","password",null, Roles.USER,
                Gender.UNKNOWN,null);
        Product product = new Product(1l,"product",123d,null, Size.XL,"material",
               null,null );
        Cart cart = new Cart(null,user,null,product,null);
        CartDto cartDto = cartService.mapToDto(cart);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        assertEquals(cartDto.getProduct(),cartService.addNewProduct(user.getId(),product.getId()).getProduct());

    }

    @Test
    public void removeProduct() {
        User user = new User(1l,"name","surname","email","password",null, Roles.USER,
                Gender.UNKNOWN,null);
        Product product = new Product(1l,"product",123d,null, Size.XL,"material",
                null,null );
        Cart cart = new Cart(null,user,null,product,null);
        CartDto cartDto = cartService.mapToDto(cart);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Mockito.when(cartRepository.findByUserAndRdtIsNull(user)).thenReturn(cart);
        Mockito.when(userRepository.findByIdAndRdtIsNull(user.getId())).thenReturn(user);
        Mockito.when(productRepository.findByIdAndRdtIsNull(product.getId())).thenReturn(product);
        assertEquals(null,cartService.removeProduct(user.getId(),product.getId()).getProduct());
    }
}