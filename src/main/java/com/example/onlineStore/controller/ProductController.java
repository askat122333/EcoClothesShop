package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/all")
    public List<ProductDto> getAll(){
        return productService.getAll();
    }

    @PostMapping("/create")
    public ProductDto addNewProduct(@RequestBody Product product){
        return productService.create(product);
    }

    @PutMapping("/update/{id}")
    public ProductDto updateUser(@PathVariable Long id,
                              @RequestBody ProductDto dto){
        return productService.update(id,dto);

    }
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id){
        return productService.deleteById(id);
    }
}
