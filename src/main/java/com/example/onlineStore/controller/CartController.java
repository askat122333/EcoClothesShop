package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    @GetMapping("/{id}")
    public CartDto getById(@PathVariable @Min(1) Long id) throws CartNotFoundException {
        return cartService.getById(id);
    }

    @GetMapping("/all")
    public List<CartDto> getAll() throws CartNotFoundException {
        return cartService.getAll();
    }

    @PostMapping("/create")
    public CartDto addNewCart(@RequestBody CartDto dto){
        return cartService.create(dto);
    }

    @PutMapping("/update/{id}")
    public CartDto updateCart(@PathVariable Long id,
                                      @RequestBody CartDto dto) throws CartNotFoundException {
        return cartService.update(id,dto);
    }
    @PutMapping("/addNewProduct")
    public CartDto addNewProduct(@RequestParam("userId")Long userId,
                                 @RequestParam("productId")Long productId) throws UserNotFoundException, CartNotFoundException {
        return cartService.addNewProduct(userId,productId);
    }

    @PutMapping("/removeProduct")
    public CartDto removeProduct(@RequestParam Long userId ,
                                 @RequestParam Long productId) throws CartNotFoundException {
        return cartService.removeProduct(userId,productId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws CartNotFoundException {
        return cartService.deleteById(id);
    }
}
