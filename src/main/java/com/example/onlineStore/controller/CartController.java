package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.exceptions.CartNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('admin:read')")
    public CartDto getById(@PathVariable @Min(1) Long id) throws CartNotFoundException {
        return cartService.getById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:update')")
    public List<CartDto> getAll() throws CartNotFoundException {
        return cartService.getAll();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:read')")
    public CartDto addNewCart(@RequestBody CartDto dto){
        return cartService.create(dto);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public CartDto updateCart(@PathVariable Long id,
                                      @RequestBody CartDto dto) throws CartNotFoundException {
        return cartService.update(id,dto);
    }
    @PutMapping("/addNewProduct")
    @PreAuthorize("hasAuthority('admin:read')")
    public CartDto addNewProduct(@RequestParam("userId")Long userId,
                                 @RequestParam("productId")Long productId) throws UserNotFoundException, CartNotFoundException {
        return cartService.addNewProduct(userId,productId);
    }

    @PutMapping("/removeProduct")
    @PreAuthorize("hasAuthority('admin:read')")
    public CartDto removeProduct(@RequestParam Long userId ,
                                 @RequestParam Long productId) throws CartNotFoundException {
        return cartService.removeProduct(userId,productId);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteById(@PathVariable Long id) throws CartNotFoundException {
        return cartService.deleteById(id);
    }
    @GetMapping("/findByUser/{id}")
    public List<Product> findByUser(@PathVariable Long id){
        CartDto dto = cartService.findByUser(id);
        return dto.getProducts();
    }
}
