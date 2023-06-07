package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CreateStripeCustomerDto;
import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.dto.StripeDto;
import com.example.onlineStore.entity.*;
import com.example.onlineStore.enums.PaymentStatus;
import com.example.onlineStore.exceptions.*;
import com.example.onlineStore.repository.OrderRepository;
import com.example.onlineStore.repository.PaymentCardRepository;
import com.example.onlineStore.repository.PaymentRepository;
import com.example.onlineStore.service.PaymentService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import io.swagger.v3.core.util.Json;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.*;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
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
            log.error("Метод getById(Payment), Exception: Платеж с таким id "+id+" не найден.");
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
          log.error("Метод getAll(Payment), Exception: В базе нет платежей.");
          throw new PaymentNotFoundException("В базе нет платежей.");
      }
      List<PaymentDto> paymentDtoList = new ArrayList<>();
        for (Payment payment:paymentList) {
            paymentDtoList.add(mapToDto(payment));
        }
        return paymentDtoList;
    }

    @Override
    public PaymentDto create(@Valid PaymentDto dto) {
        Payment payment = Payment.builder()
                .sum(dto.getSum())
                .cardNum(dto.getCardNum())
                .status(dto.getStatus())
                .user(dto.getUser())
                .paymentTime(dto.getPaymentTime())
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Payment> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод create(Payment): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto update(Long id,@Valid PaymentDto dto) throws PaymentNotFoundException {

            Payment payment = getByIdEntity(id);
            if(payment==null){
                log.error("Метод update(Payment), Exception: Платеж с таким id "+id+" не найден.");
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
                log.warn("Метод update(Payment): "+errorMessages);
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
            log.error("Метод deleteById(Payment), Exception: Платеж с таким id "+id+" не найден.");
            throw new PaymentNotFoundException("Платеж с таким id "+id+" не найден.");
        }

    }

    @Transactional
    @Override
    public String makePayment( Long orderId) throws OrderNotFoundException, UserNotFoundException, PaymentCardNotFoundException {
        Order order = orderRepository.findByIdAndRdtIsNull(orderId);
        if(order==null){
            log.error("Метод makePayment(Payment), Exception: Заказ с id "+orderId+" не найден.");
            throw new OrderNotFoundException("Заказ с id "+orderId+" не найден.");
        }
        User user = order.getUser();
        if(user==null){
            log.error("Метод makePayment(Payment), Exception: У данного заказа отсутствует пользователь.");
            throw new UserNotFoundException("У данного заказа отсутствует пользователь.");
        }
        Payment payment = order.getPayment();
        PaymentCard paymentCard = paymentCardRepository.findByUserAndRdtIsNull(user);
        if(paymentCard==null){
            log.error("Метод makePayment(Payment), Exception: У данного пользователя отсутствует карта оплаты.");
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
            payment.setReceipt(receiptRecording(order.getProducts()));
            order.setRdt(LocalDate.now());
            paymentRepository.save(payment);
            return "Оплата прошла успешно!"+"\nЧек : \n"+payment.getReceipt()+
                    "\nВаш баланс : " + paymentCard.getBalance();

        } else if ( !payment.getCardNum().isEmpty()) {

            return "Заказ уже оформлен";

        } else {

            return "Не достаточно средств.";

        }
    }

    @Override
    @Transactional
   public String stripePayment(Long orderId , StripeDto dto) throws StripeException, OrderNotFoundException, UserNotFoundException {
        Stripe.apiKey = "sk_test_51NDXxjGOVlOOV4yQONVbA1UvZMDnrwrSWaTdAAyfXyPNFcdXOywDnmTYUXRF6sEXjDhZl9H7LlFECq5fIiR4g3ec009HqtR6L9";

        Order order = orderRepository.findByIdAndRdtIsNull(orderId);
        if(order==null){
            log.error("Метод makePayment(Payment), Exception: Заказ с id "+orderId+" не найден.");
            throw new OrderNotFoundException("Заказ с id "+orderId+" не найден.");
        }
        User user = order.getUser();
        if(user==null){
            log.error("Метод makePayment(Payment), Exception: У данного заказа отсутствует пользователь.");
            throw new UserNotFoundException("У данного заказа отсутствует пользователь.");
        }

        Payment payment = order.getPayment();

        Double orderSum = order.getSum()*100;

        Integer sum = Double.valueOf(orderSum).intValue();

        Map<String,Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", sum);
        chargeParams.put("currency","USD");
        chargeParams.put("source",createCardToken(dto.getCardNumber(),dto.getExpMonth(),dto.getExpYear(),dto.getCvc()));
        chargeParams.put("description",dto.getDescription());
        chargeParams.put("receipt_email",dto.getReceiptEmail());

       Charge charge = Charge.create(chargeParams);
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(charge);

            JSONObject json = new JSONObject(jsonString);


        JSONObject card = json.getJSONObject("payment_method_details").getJSONObject("card");

            String email = json.getString("receipt_email");
            Integer amount = json.getInt("amount");
            String currency = json.getString("currency");
            String status = json.getString("status");
            String brand = card.getString("brand");
            String last4 = card.getString("last4");
            String response = "Статус оплаты: "+status+
                    "\nТип карты: "+brand+
                    "\nНомер карты: "+last4+
                    "\nСумма: "+(amount/100)+" "+currency+
                    "\nEmail клиента: "+email;

        payment.setSum((double) (sum / 100));
        payment.setUser(user);
        payment.setPaymentTime(LocalDate.now());
        payment.setStatus(PaymentStatus.PAID);
        payment.setCardNum(last4);
        payment.setReceipt(response);
        order.setRdt(LocalDate.now());
        paymentRepository.save(payment);
        orderRepository.save(order);

            return response;

    }

    private String createCardToken(String cardNumber, String expMonth, String expYear, String cvc) throws StripeException {
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", cardNumber);
        cardParams.put("exp_month", expMonth);
        cardParams.put("exp_year", expYear);
        cardParams.put("cvc", cvc);

        Map<String, Object> tokenParams = new HashMap<>();
        tokenParams.put("card", cardParams);

        com.stripe.model.Token token = com.stripe.model.Token.create(tokenParams);

        return token.getId();
    }

    @Override
    public String addCustomerAndCard(CreateStripeCustomerDto dto) throws StripeException {
        Stripe.apiKey = "sk_test_51NDXxjGOVlOOV4yQONVbA1UvZMDnrwrSWaTdAAyfXyPNFcdXOywDnmTYUXRF6sEXjDhZl9H7LlFECq5fIiR4g3ec009HqtR6L9";
        Map<String,Object> customerParameter = new HashMap<>();
        customerParameter.put("name",dto.getName());
        customerParameter.put("email",dto.getEmail());
        Customer newCustomer = Customer.create(customerParameter);
        String customerId = newCustomer.getId();//этот айди должен быть в базе с его использованием можно проводить оплату

        Map<String,Object> cardParameter = new HashMap<>();
        cardParameter.put("number",dto.getCardNumber());
        cardParameter.put("exp_month",dto.getExp_month());
        cardParameter.put("exp_year",dto.getExp_year());
        cardParameter.put("cvc",dto.getCvc());
        Map<String,Object> token = new HashMap<>();
        token.put("card",cardParameter);

        Token token2 = Token.create(token);
        Map<String,Object> source = new HashMap<>();
        source.put("source",token2.getId());

        newCustomer.getSources().create(source);
        return "ПОльзователь с именем: "+newCustomer.getName()+" добавлен в систему.";
    }

    public static void chargeForDbCustomer() throws StripeException {
        Map<String,Object> charge = new HashMap<>();
        charge.put("amount","500");
        charge.put("currency","usd");
        charge.put("customer","cus_NzXHJEmBjUgnCt");
        Charge charge2 = Charge.create(charge);
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(charge2));
    }

    public Double isHaveDiscount(List<Product> products){
        double discountSum= 0d;
        for (Product product : products) {
            if (product.getDiscount() != null) {
                discountSum += (product.getPrice()
                        - (product.getPrice() * product.getDiscount().getDiscount() / 100));
            }else {
                discountSum += product.getPrice();
            }
        }
        return discountSum;
    }
    public String receiptRecording(List<Product> products){
        List<String> stringList = new ArrayList<>();
        Double sum = 0d;
        for (Product product:products) {
            stringList.add(product.getName());
            sum += product.getPrice();
        }
        return "Продукты : "+stringList.toString()+
                "\nОбщая сумма : "+sum;
    }


}
