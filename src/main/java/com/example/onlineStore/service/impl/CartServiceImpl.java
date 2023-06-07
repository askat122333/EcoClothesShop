package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.CartStatus;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.CartService;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public CartDto mapToDto(Cart cart) {
        return CartDto.builder().id(cart.getId())
                .user(cart.getUser())
                .cartStatus(cart.getCartStatus())
                .sum(cart.getSum())
                .products(cart.getProducts())
                .rdt(cart.getRdt())
                .build();
    }

    @Override
    public CartDto getById(Long id) throws CartNotFoundException {
        try {
            Cart cart = cartRepository.findByIdAndRdtIsNull(id);
            return mapToDto(cart);
        } catch (NullPointerException e) {
            log.error("Метод getById(Cart), Exception: Корзина с id " + id + " не найдена.");
            throw new CartNotFoundException("Корзина с id " + id + " не найдена.");
        }

    }

    @Override
    public Cart getByIdEntity(Long id) {
        return cartRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<CartDto> getAll() throws CartNotFoundException {
        List<Cart> cartList = cartRepository.findAllByRdtIsNull();
        if (cartList.isEmpty()) {
            log.error("Метод getAll(Cart), Exception: В базе нет корзин.");
            throw new CartNotFoundException("В базе нет корзин.");
        }
        List<CartDto> cartDtoList = new ArrayList<>();
        for (Cart cart : cartList) {
            cartDtoList.add(mapToDto(cart));
        }
        return cartDtoList;
    }

    @Override
    public CartDto create(@Valid CartDto dto) {
        Cart cart = Cart.builder()
                .sum(dto.getSum())
                .products(dto.getProducts())
                .user(dto.getUser())
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Cart>> violations = validator.validate(cart);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Cart> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод create(Cart): " + errorMessages);
            }
            throw new ValidException(errorMessages);
        }
        return mapToDto(cartRepository.save(cart));
    }
    @Transactional
    @Override
    public CartDto update(Long id, @Valid CartDto dto) throws CartNotFoundException {
        Cart cart = getByIdEntity(id);
        if (cart == null) {
            log.error("Метод update(Cart), Exception: Корзина с id " + id + " не найдена.");
            throw new CartNotFoundException("Корзина с id " + id + " не найдена.");
        }
        if (dto.getUser() != null) {
            cart.setUser(dto.getUser());
        }
        if (cart.getSum() != null) {
            cart.setSum(dto.getSum());
        }
        if (dto.getProducts() != null) {
            cart.setProducts(dto.getProducts());
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Cart>> violations = validator.validate(cart);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Cart> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(Cart): " + errorMessages);
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
            return "Корзина с id: " + id + " была удалена.";
        } catch (NullPointerException e) {
            log.error("Метод deleteById(Cart), Exception: Корзина с id " + id + " не найдена.");
            throw new CartNotFoundException("Корзина с id " + id + " не найдена.");
        }

    }

    @Transactional
    @Override
    public CartDto addNewProduct(Long userId, Long productId) throws UserNotFoundException, CartNotFoundException {
        User user = userRepository.findByIdAndRdtIsNull(userId);
        if (user == null) {
            log.error("Метод addNewProduct(Cart), Exception: Пользователь с id " + userId + " не найден.");
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден.");
        }
        Cart cart = cartRepository.findByUserAndRdtIsNull(user);
        Product product = productRepository.findByIdAndRdtIsNull(productId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            List<Product> products = new ArrayList<>();
            products.add(product);
            newCart.setProducts(products);
            newCart.setCartStatus(CartStatus.PROCESS);
            newCart.setSum(getSum(products));
            cartRepository.save(newCart);
            return mapToDto(newCart);
        } else if (cart.getCartStatus() == CartStatus.FINISHED) {
            System.out.println("Изменения не могут быть выполнены.\nКарзина уже в заказе");
            return mapToDto(cart);
        } else {
            List<Product> products = cart.getProducts();
            products.add(product);
            cart.setProducts(products);
            cart.setSum(getSum(products));
            cartRepository.save(cart);
            return mapToDto(cart);
        }

    }

    private Double getSum(List<Product> products) {
        Double sum = 0d;
        for (int i = 0; i < products.size(); i++) {
            sum += products.get(i).getPrice();
        }
        return sum;
    }

    @Transactional
    @Override
    public CartDto removeProduct(Long userId, Long productId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserAndRdtIsNull(userRepository.findByIdAndRdtIsNull(userId));
        if (cart == null) {
            log.error("Метод removeProduct(Cart), Exception: У данного пользователя нет корзины с заказами.");
            throw new CartNotFoundException("У данного пользователя нет корзины с заказами.");
        } else if (cart.getCartStatus() == CartStatus.FINISHED) {
            System.out.println("Изменения не могут быть выполнены.\nКарзина уже в заказе");
            return mapToDto(cart);
        } else {
            // cart.getProducts().removeIf(product -> product.getId() == productId);
            List<Product> products = new ArrayList<>();
            products.addAll(cart.getProducts());
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == productId) {
                    products.remove(products.get(i));
                    break;
                }
            }
            cart.setProducts(products);
            cart.setSum(getSum(cart.getProducts()));
            cartRepository.save(cart);
            return mapToDto(cart);
        }
    }

    @Override
    public CartDto findByUser(Long id) {
        Cart cart = cartRepository.findByUserId(id);
        return mapToDto(cart);
    }
}
