package com.example.onlineStore.controller;

import com.example.onlineStore.dto.DtoForBalance;
import com.example.onlineStore.dto.PaymentCardDto;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.PaymentCardService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/paymentCard")
public class PaymentCardController {
    private final PaymentCardService paymentCardService;
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public PaymentCardDto getById(@PathVariable @Min(1) Long id) throws PaymentCardNotFoundException {
        return paymentCardService.getById(id);
    }
    @GetMapping("/balance/{userId}")
    public DtoForBalance getUserBalance(@PathVariable @Min(1) Long userId) throws PaymentCardNotFoundException {
        return paymentCardService.getUserBalance(userId);
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:update')")
    public List<PaymentCardDto> getAll() throws PaymentCardNotFoundException {
        return paymentCardService.getAll();
    }
    @PostMapping("/create/{userId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String addNewPaymentCard(@RequestBody PaymentCardDto dto
            , @PathVariable Long userId) throws UserNotFoundException {
        return paymentCardService.create(dto, userId);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public PaymentCardDto updateCategory(@PathVariable Long id,
                                      @RequestBody PaymentCardDto dto) throws PaymentCardNotFoundException {
        return paymentCardService.update(id,dto);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteById(@PathVariable Long id) throws PaymentCardNotFoundException {
        return paymentCardService.deleteById(id);
    }
}
