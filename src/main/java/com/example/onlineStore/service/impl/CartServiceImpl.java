package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public CartDto getById(Long id) throws CartNotFoundException {
        try{
            Cart cart = cartRepository.findByIdAndRdtIsNull(id);
            return mapToDto(cart);
        }catch (NullPointerException e){
            throw new CartNotFoundException("Корзина с id "+id+" не найдена.");
        }

    }

    @Override
    public Cart getByIdEntity(Long id) {
        return cartRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<CartDto> getAll() throws CartNotFoundException {
        List<Cart> cartList = cartRepository.findAllByRdtIsNull();
        if(cartList.isEmpty()){
            throw new CartNotFoundException("В базе нет корзин.");
        }
        List<CartDto> cartDtoList = new ArrayList<>();
        for (Cart cart:cartList) {
            cartDtoList.add(mapToDto(cart));
        }
        return cartDtoList;
    }

    @Override
    public CartDto create(@Valid Cart cart) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Cart>> violations = validator.validate(cart);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Cart> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
            }
            throw new ValidException(errorMessages);
        }
        return mapToDto(cartRepository.save(cart));
    }

    @Override
    public CartDto update(Long id,@Valid CartDto dto) throws CartNotFoundException {
        Cart cart = getByIdEntity(id);
        if(cart == null){
            throw new CartNotFoundException("Корзина с id "+id+" не найдена.");
        }
        if(dto.getUser()!=null){
            cart.setUser(dto.getUser());
        }
        if(cart.getSum()!=null){
            cart.setSum(dto.getSum());
        }
        if(dto.getProducts()!=null){
            cart.setProducts(dto.getProducts());
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Cart>> violations = validator.validate(cart);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Cart> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(cartRepository.save(cart));
    }

    @Override
    public String deleteById(Long id) throws CartNotFoundException {
        try {
            Cart cart = getByIdEntity(id);
            cart.setRdt(LocalDate.now());
            cartRepository.save(cart);
            return "Корзина с id: "+id+" была удалена.";
        }catch (NullPointerException e){
            throw new CartNotFoundException("Корзина с id "+id+" не найдена.");
        }

    }


    @Override
    public CartDto addNewProduct(Long userId,Long productId) throws UserNotFoundException, CartNotFoundException {
        User user  = userRepository.findByIdAndRdtIsNull(userId);
        if(user==null){
            throw new UserNotFoundException("Пользователь с id "+userId+" не найден.");
        }
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
    public CartDto removeProduct(Long userId, Long productId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserAndRdtIsNull(userRepository.findByIdAndRdtIsNull(userId));
        if(cart==null){
            throw new CartNotFoundException("У данного пользователя нет корзины с заказами.");
        }
       cart.getProducts().removeIf(product -> product.getId() == productId);
       cart.setSum(getSum(cart.getProducts()));
        cartRepository.save(cart);
        return mapToDto(cart);
    }
}
