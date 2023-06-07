package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.dto.PayPalDto;
import com.example.onlineStore.entity.*;
import com.example.onlineStore.enums.CartStatus;
import com.example.onlineStore.enums.PaymentStatus;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.*;
import com.example.onlineStore.service.OrderService;
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
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    public OrderDto mapToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUser(),
                order.getProducts(),
                order.getAddress(),
                order.getPayment(),
                order.getSum(),
                order.getOrderTime(),
                order.getRdt()
        );
    }

    @Override
    public OrderDto getById(Long id) throws OrderNotFoundException {
        try {
            Order order = orderRepository.findByIdAndRdtIsNull(id);
            return mapToDto(order);
        }catch (NullPointerException e){
            log.error("Метод getById(Order), Exception: Заказ с id "+id+" не найден.");
            throw new OrderNotFoundException("Заказ с id "+id+" не найден.");
        }

    }


    @Override
    public Order getByIdEntity(Long id) {
        return orderRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<OrderDto> getAll() throws OrderNotFoundException {
        List<Order> orderList = orderRepository.findAllByRdtIsNull();
        if(orderList.isEmpty()){
            log.error("Метод getAll(Order), Exception: В базе нет заказов.");
            throw new OrderNotFoundException("В базе нет заказов.");
        }
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order:orderList) {
            orderDtoList.add(mapToDto(order));
        }
        return orderDtoList;
    }

    @Override
    public OrderDto create(Long userId, String address) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserAndRdtIsNull(userRepository.findByIdAndRdtIsNull(userId));
        if (cart==null){
            log.error("Метод create(Order), Exception: У данного пользователя нет корзины с товарами.");
            throw new CartNotFoundException("У данного пользователя нет корзины с товарами.");
        }
        Order order = new Order();
        order.setUser(userRepository.findByIdAndRdtIsNull(userId));
        List<Product> products = new ArrayList<>();
        products.addAll(cart.getProducts());
        order.setProducts(products);
        order.setAddress(address);
        cart.setCartStatus(CartStatus.FINISHED);
        cart.setRdt(LocalDate.now());
        order.setSum(cart.getSum());
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        order.setPayment(payment);
        order.setOrderTime(LocalDate.now());
        orderRepository.save(order);
        return mapToDto(order);
    }

    @Transactional
    @Override
    public OrderDto update(Long id,@Valid OrderDto dto) throws OrderNotFoundException {
        Order order = getByIdEntity(id);
        if(order==null){
            log.error("Метод update(Order), Exception: Заказ с id "+id+" не найден.");
            throw new OrderNotFoundException("Заказ с id "+id+" не найден.");
        }
        if(dto.getUser()!=null){
            order.setUser(dto.getUser());
        }
        if(dto.getProducts()!=null){
            order.setProducts(dto.getProducts());
        }
        if(dto.getAddress()!=null){
            order.setAddress(dto.getAddress());
        }
        if(dto.getPayment()!=null){
            order.setPayment(dto.getPayment());
        }
        if(dto.getSum()!=null){
            order.setSum(dto.getSum());
        }
        if(dto.getOrderTime()!=null){
            order.setOrderTime(dto.getOrderTime());
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Order> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(Order): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(orderRepository.save(order));
    }

    @Override
    public String deleteById(Long id) throws OrderNotFoundException {
        try {
            Order order = getByIdEntity(id);
            order.setRdt(LocalDate.now());
            orderRepository.save(order);
            return "Заказ с id: "+id+" был удален.";
        }catch (NullPointerException e){
            log.error("Метод deleteById(Order), Exception: Заказ с id "+id+" не найден.");
            throw new OrderNotFoundException("Заказ с id "+id+" не найден.");
        }

    }

    @Transactional
    @Override
    public OrderDto quickCreate(Long userId, Long productId, String address) throws ProductNotFoundException {
        Order order = new Order();
        order.setUser(userRepository.findByIdAndRdtIsNull(userId));
        Product product = productRepository.findByIdAndRdtIsNull(productId);
        if(product==null){
            log.error("Метод quickCreate(Order), Exception: Продукт с таким id "+productId+" не найден в базе.");
            throw new ProductNotFoundException("Продукт с таким id "+productId+" не найден в базе.");
        }

        order.setProducts(List.of(product));
        order.setAddress(address);
        order.setSum(product.getPrice());
        order.setOrderTime(LocalDate.now());
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        order.setPayment(payment);
        orderRepository.save(order);
        return mapToDto(order);
    }

    @Override
    public PayPalDto findByUserId(Long id) {
        Order order = orderRepository.findByUserIdAndRdtIsNull(id);
        return PayPalDto.builder()
                .user(order.getUser().getName())
                .sum(order.getSum())
                .address(order.getAddress())
                .products(order.getProducts())
                .id(order.getId())
                .build();

    }
}
