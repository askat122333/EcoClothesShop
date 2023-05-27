package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.Gender;
import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.enums.Roles;
import com.example.onlineStore.enums.Size;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
  public   void addNewProduct() throws UserNotFoundException, CartNotFoundException {
        User user = new User();
        user.setId(1l);
        Product product = new Product(1l,"name",123d,Size.S,"material",
                null, ProductType.REGULAR, LocalDate.now(),null,null,null);
        List<Product> products = new ArrayList<>();
        products.add(product);
        Cart cart = new Cart(null,user,null,products,null);
        CartDto cartDto = cartService.mapToDto(cart);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Mockito.when(userRepository.findByIdAndRdtIsNull(user.getId())).thenReturn(user);
        Mockito.when(productRepository.findByIdAndRdtIsNull(product.getId())).thenReturn(product);
        assertEquals(cartDto.getProducts(),cartService.addNewProduct(user.getId(),product.getId()).getProducts());
        assertEquals(123d,cartService.addNewProduct(user.getId(),product.getId()).getSum());

    }

    @Test
    public void removeProduct() throws CartNotFoundException {
        User user = new User();
        user.setId(1l);
        Product product = new Product(1l,"name",123d,Size.S,"material",
                null, ProductType.REGULAR, LocalDate.now(),null,null,null);
        List<Product> products = new ArrayList<>();
        products.add(product);
        Cart cart = new Cart(null,user,null,products,null);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Mockito.when(cartRepository.findByUserAndRdtIsNull(user)).thenReturn(cart);
        Mockito.when(userRepository.findByIdAndRdtIsNull(user.getId())).thenReturn(user);
        Mockito.when(productRepository.findByIdAndRdtIsNull(product.getId())).thenReturn(product);
        boolean isTrue = false;
        CartDto cartDto = cartService.removeProduct(1l,1l);
        if (cartDto.getProducts().isEmpty()) {
            isTrue = true;
        }
        assertEquals(true,isTrue);
    }
}