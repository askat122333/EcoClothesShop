package com.example.onlineStore.controller;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.dto.PayPalDto;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public OrderDto getById(@PathVariable @Min(1) Long id) throws OrderNotFoundException {
        return orderService.getById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:update')")
    public List<OrderDto> getAll() throws OrderNotFoundException {
        return orderService.getAll();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:read')")
    public OrderDto addNewOrder(@RequestParam("userId") Long userId,
                                @RequestParam("address") String address) throws CartNotFoundException {
        return orderService.create(userId, address);
    }

    @PostMapping("/quickCreate")
    @PreAuthorize("hasAuthority('admin:read')")
    public OrderDto addNewQuickOrder(@RequestParam("userId") Long userId,
                                     @RequestParam("productId") Long productId,
                                     @RequestParam("address") String address) throws ProductNotFoundException {
        return orderService.quickCreate(userId, productId, address);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public OrderDto updateOrder(@PathVariable Long id,
                                @RequestBody OrderDto dto) throws OrderNotFoundException {
        return orderService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteById(@PathVariable Long id) throws OrderNotFoundException {
        return orderService.deleteById(id);
    }
    @GetMapping("/byUserId/{id}")
    public PayPalDto getByUserId(@PathVariable Long id){
        return orderService.findByUserId(id);
    }
}
