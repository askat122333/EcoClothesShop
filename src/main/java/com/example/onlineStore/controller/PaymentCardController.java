package com.example.onlineStore.controller;

import com.example.onlineStore.dto.PaymentCardDto;
import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.service.PaymentCardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/paymentCard")
public class PaymentCardController {
    private final PaymentCardService paymentCardService;
    @GetMapping("/{id}")
    public PaymentCardDto getById(@PathVariable Long id) throws PaymentCardNotFoundException {
        return paymentCardService.getById(id);
    }
    @GetMapping("/all")
    public List<PaymentCardDto> getAll() throws PaymentCardNotFoundException {
        return paymentCardService.getAll();
    }
    @PostMapping("/create")
    public PaymentCardDto addNewPaymentCard(@RequestBody PaymentCard paymentCard){
        return paymentCardService.create(paymentCard);
    }
    @PutMapping("/update/{id}")
    public PaymentCardDto updateCategory(@PathVariable Long id,
                                      @RequestBody PaymentCardDto dto) throws PaymentCardNotFoundException {
        return paymentCardService.update(id,dto);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws PaymentCardNotFoundException {
        return paymentCardService.deleteById(id);
    }
}
