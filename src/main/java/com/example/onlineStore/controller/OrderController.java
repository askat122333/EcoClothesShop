package com.example.onlineStore.controller;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.service.OOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OOrderService orderService;

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
    public OrderDto addNewOrder(@RequestParam Long userId) {
        return orderService.create(userId);
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
