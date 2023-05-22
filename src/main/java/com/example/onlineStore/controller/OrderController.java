package com.example.onlineStore.controller;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @GetMapping("/all")
    public List<OrderDto> getAll() {
        return orderService.getAll();
    }
//TODO
    @PostMapping("/create")
    public OrderDto addNewOrder(@RequestParam("userId") Long userId,
                                @RequestParam("address")String address) {
        return orderService.create(userId,address);
    }

    @PutMapping("/update/{id}")
    public OrderDto updateOrder(@PathVariable Long id,
                                @RequestBody OrderDto dto) {
        return orderService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        return orderService.deleteById(id);
    }
}
