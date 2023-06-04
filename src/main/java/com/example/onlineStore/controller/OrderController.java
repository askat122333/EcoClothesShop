package com.example.onlineStore.controller;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.dto.PayPalDto;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.OrderNotFoundException;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.service.OrderService;
import lombok.AllArgsConstructor;
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
    public OrderDto getById(@PathVariable @Min(1) Long id) throws OrderNotFoundException {
        return orderService.getById(id);
    }

    @GetMapping("/all")
    public List<OrderDto> getAll() throws OrderNotFoundException {
        return orderService.getAll();
    }
    @PostMapping("/create")
    public OrderDto addNewOrder(@RequestParam("userId") Long userId,
                                @RequestParam("address")String address) throws CartNotFoundException {
        return orderService.create(userId,address);
    }

    @PutMapping("/update/{id}")
    public OrderDto updateOrder(@PathVariable Long id,
                                @RequestBody OrderDto dto) throws OrderNotFoundException {
        return orderService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws OrderNotFoundException {
        return orderService.deleteById(id);
    }
    @GetMapping("/byUserId/{id}")
    public PayPalDto getByUserId(@PathVariable Long id){
        return orderService.findByUserId(id);
    }
    @PostMapping("/quickCreate")
    public OrderDto quickCreate(@RequestParam("userId") Long userId,
                                @RequestParam("productId") Long productId,
                                @RequestParam("address") String address) throws ProductNotFoundException {
        return orderService.quickCreate(userId,productId,address);
    }
}
