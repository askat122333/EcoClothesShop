package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/pictures")
@AllArgsConstructor
public class PicsController {

       private ProductService productService;

    @GetMapping
    public String getAll(Model model) throws ProductNotFoundException {

        model.addAttribute("products", productService.getAll());

        return "/books";
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Product product = productService.getByIdEntity(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(product.getImage());
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @GetMapping("/{id}")
    public String getBook(@PathVariable Long id, Model model) {
        Product product = productService.getByIdEntity(id);

        model.addAttribute("product", product);

        return "/book/show";
    }
}
