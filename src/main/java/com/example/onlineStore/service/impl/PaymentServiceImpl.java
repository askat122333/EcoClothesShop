package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.entity.*;
import com.example.onlineStore.enums.PaymentStatus;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.PaymentNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.repository.PaymentCardRepository;
import com.example.onlineStore.repository.PaymentRepository;
import com.example.onlineStore.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentCardRepository paymentCardRepository;

    private PaymentDto mapToDto (Payment payment){
        return new PaymentDto(
          payment.getId(),
          payment.getSum(),
          payment.getStatus(),
          payment.getPaymentTime(),
          payment.getCardNum(),
          payment.getUser(),
          payment.getRdt()
        );
    }
    @Override
    public PaymentDto getById(Long id) throws PaymentNotFoundException {
        try{
            Payment payment = paymentRepository.findByIdAndRdtIsNull(id);
            return mapToDto(payment);
        }catch (NullPointerException e){
            throw new PaymentNotFoundException("Платеж с таким id "+id+" не найден.");
        }

    }

    @Override
    public Payment getByIdEntity(Long id) {
     return paymentRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<PaymentDto> getAll() throws PaymentNotFoundException {
      List<Payment> paymentList = paymentRepository.findAllByRdtIsNull();
      if(paymentList.isEmpty()){
          throw new PaymentNotFoundException("В базе нет платежей.");
      }
      List<PaymentDto> paymentDtoList = new ArrayList<>();
        for (Payment payment:paymentList) {
            paymentDtoList.add(mapToDto(payment));
        }
        return paymentDtoList;
    }

    @Override
    public PaymentDto create(Payment payment) {

        return mapToDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto update(Long id, PaymentDto dto) throws PaymentNotFoundException {

            Payment payment = getByIdEntity(id);
            if(payment==null){
                throw new PaymentNotFoundException("Платеж с таким id "+id+" не найден.");
            }
            if (dto.getSum() != null) {
                payment.setSum(dto.getSum());
            }
            if (dto.getStatus() != null) {
                payment.setStatus(dto.getStatus());
            }
            if (dto.getPaymentTime() != null) {
                payment.setPaymentTime(dto.getPaymentTime());
            }
            if (dto.getCardNum() != null) {
                payment.setCardNum(dto.getCardNum());
            }
            if (dto.getUser() != null) {
                payment.setUser(dto.getUser());
            }
            return mapToDto(payment);


    }
    @Override
    public String deleteById(Long id) throws PaymentNotFoundException {
        try{
            Payment payment = getByIdEntity(id);
            payment.setRdt(LocalDate.now());
            paymentRepository.save(payment);
            return "Платеж с id: "+id+" был удален.";
        }catch (NullPointerException e){
            throw new PaymentNotFoundException("Платеж с таким id "+id+" не найден.");
        }

    }

    @Override
    public String makePayment( Long orderId) throws OrderNotFoundException, UserNotFoundException, PaymentCardNotFoundException {
        Order order = orderRepository.findByIdAndRdtIsNull(orderId);
        if(order==null){
            throw new OrderNotFoundException("Заказ с id "+orderId+" не найден.");
        }
        User user = order.getUser();
        if(user==null){
            throw new UserNotFoundException("У данного заказа отсутствует пользователь.");
        }
        Payment payment = order.getPayment();
        PaymentCard paymentCard = paymentCardRepository.findByUserAndRdtIsNull(user);
        if(paymentCard==null){
            throw new PaymentCardNotFoundException("У данного пользователя отсутствует карта оплаты.");
        }
        Double balance = paymentCard.getBalance();
        Double orderSum = isHaveDiscount(order.getProducts());
        if (payment.getCardNum() == null && balance>=orderSum) {
            paymentCard.setBalance(balance-orderSum);
            paymentCardRepository.save(paymentCard);
            payment.setSum(orderSum);
            payment.setUser(user);
            payment.setPaymentTime(LocalDate.now());
            payment.setStatus(PaymentStatus.PAID);
            String num = paymentCard.getCardNum().substring(8);
            payment.setCardNum(num);
            paymentRepository.save(payment);
            return "Оплата прошла успешно!";

        } else if ( !payment.getCardNum().isEmpty()) {

            return "Заказ уже оформлен";

        } else {

            return "Не достаточно средств.";

        }
    }
    public Double isHaveDiscount(List<Product> products){
        double discountSum= 0d;
        for (Product product : products) {
            if (product.getDiscount() != null) {
                discountSum += (product.getPrice()
                        - (product.getPrice() * product.getDiscount().getDiscount()));
            }else {
                discountSum += product.getPrice();
            }
        }
        return discountSum;
    }

}
