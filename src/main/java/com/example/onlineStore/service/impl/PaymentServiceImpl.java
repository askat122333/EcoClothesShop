package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.Payment;
import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.PaymentStatus;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.repository.PaymentCardRepository;
import com.example.onlineStore.repository.PaymentRepository;
import com.example.onlineStore.repository.UserRepository;
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
    private final UserRepository userRepository;
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
    public PaymentDto getById(Long id) {
        Payment payment = paymentRepository.findByIdAndRdtIsNull(id);
        return mapToDto(payment);
    }

    @Override
    public Payment getByIdEntity(Long id) {
     return paymentRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<PaymentDto> getAll() {
      List<Payment> paymentList = paymentRepository.findAllByRdtIsNull();
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
    public PaymentDto update(Long id, PaymentDto dto) {
        Payment payment = getByIdEntity(id);
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
    public String deleteById(Long id) {
        Payment payment = getByIdEntity(id);
        payment.setRdt(LocalDate.now());
        paymentRepository.save(payment);
        return "Платежь с id: "+id+" была удален.";
    }

    @Override
    public String makePayment(Long userId , Long orderId) {
        User user = userRepository.findByIdAndRdtIsNull(userId);
        Order order = null;
        for (Order desiredOrder:user.getOrder()) {
            if (desiredOrder.getId() == orderId) {
                order = desiredOrder;
            }
        }

        Payment payment = order.getPayment();
        PaymentCard paymentCard = paymentCardRepository.findByUserAndRdtIsNull(userId);
        Double balance = paymentCard.getBalance();
        Double orderSum = order.getSum();
        if (balance>=orderSum) {
           paymentCard.setBalance(balance-orderSum);
           paymentCardRepository.save(paymentCard);
           payment.setSum(orderSum);
            payment.setUser(user);
            payment.setPaymentTime(LocalDate.now());
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);

        }else {
            return "Не достаточно средств.";
        }
        return "Оплата прошла успешно!";
    }
}