package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CreateStripeCustomerDto;
import com.example.onlineStore.dto.StripeDto;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;


    @PostMapping("/makePayment")
    @PreAuthorize("hasAuthority('admin:read')")
    public String makePayment(@RequestParam("orderId") @Min(1) Long orderId) throws OrderNotFoundException, UserNotFoundException, PaymentCardNotFoundException {
        return paymentService.makePayment(orderId);
    }

    @PostMapping("/stripe/{orderId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String stripePayment(@PathVariable Long orderId,
                                @RequestBody StripeDto dto) throws StripeException, OrderNotFoundException, UserNotFoundException {
        return paymentService.stripePayment(orderId,dto);
    }
    @PutMapping("/addStripeCustomer")
    @PreAuthorize("hasAuthority('admin:read')")
    public String addStripeCustomer(@RequestBody CreateStripeCustomerDto dto) throws StripeException {
        return paymentService.addCustomerAndCard(dto);
    }
}
