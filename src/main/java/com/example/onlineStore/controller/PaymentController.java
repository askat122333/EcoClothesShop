package com.example.onlineStore.controller;

import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping("/makePayment")

    public String makePayment(@RequestParam("orderId") @Min(1) Long orderId) throws OrderNotFoundException, UserNotFoundException, PaymentCardNotFoundException {
        return paymentService.makePayment(orderId);
    }
}
