package com.example.onlineStore.controller;

import com.example.onlineStore.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping("/makePayment")
    public String makePayment(@RequestParam("userId")Long userId ,
                              @RequestParam("orderId")Long orderId){
        return paymentService.makePayment(userId, orderId);
    }
}