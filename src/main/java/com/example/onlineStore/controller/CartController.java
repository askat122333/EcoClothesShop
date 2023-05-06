package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    @GetMapping("/{id}")
    public CartDto getById(@PathVariable Long id) {
        return cartService.getById(id);
    }

    @GetMapping("/all")
    public List<CartDto> getAll(){
        return cartService.getAll();
    }

    @PostMapping("/create")
    public CartDto addNewCart(@RequestBody Cart cart){
        return cartService.create(cart);
    }

    @PutMapping("/update/{id}")
    public CartDto updateCart(@PathVariable Long id,
                                      @RequestBody CartDto dto){
        return cartService.update(id,dto);
    }
    //TODO
    @PutMapping("/addNewProduct")
    public CartDto addNewProduct(@RequestParam Long cartId ,
                                 @RequestParam Long productId){
        return cartService.addNewProduct(cartId,productId);
    }
    //TODO
    @PutMapping("/removeProduct")
    public CartDto removeProduct(@RequestParam Long cartId ,
                                 @RequestParam Long productId){
        return cartService.removeProduct(cartId,productId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id){
        return cartService.deleteById(id);
    }
}
