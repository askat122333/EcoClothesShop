package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.entity.*;
import com.example.onlineStore.enums.PaymentStatus;
import com.example.onlineStore.exceptions.*;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.repository.PaymentCardRepository;
import com.example.onlineStore.repository.PaymentRepository;
import com.example.onlineStore.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public PaymentDto create(@Valid Payment payment) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Payment> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto update(Long id,@Valid PaymentDto dto) throws PaymentNotFoundException {

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
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Payment> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
            }
            throw new ValidException(errorMessages);
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
        Double orderSum = order.getSum();
        if (!payment.getCardNum().isEmpty()) {

            return "Заказ уже оформлен";

        } else if (payment.getCardNum().isEmpty() && balance>=orderSum) {
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
        } else {
            return "Не достаточно средств.";
        }
    }
}
