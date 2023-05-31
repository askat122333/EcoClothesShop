package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CreateStripeCustomerDto;
import com.example.onlineStore.dto.StripeDto;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;

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

    @GetMapping("/stripe/{id}")
    public String stripePayment(@PathVariable Long id,
                                @RequestBody StripeDto dto) throws StripeException {
        return paymentService.stripePayment(id,dto);
    }
    @PostMapping("/addStripeCustomer")
    public String addStripeCustomer(@RequestBody CreateStripeCustomerDto dto) throws StripeException {
        return paymentService.addCustomerAndCard(dto);
    }
}
