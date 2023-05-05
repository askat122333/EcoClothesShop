package com.example.onlineStore.service;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductService {
    ProductDto getById(Long id);

    Product getByIdEntity(Long id);

    List<ProductDto> getAll();

    ProductDto create(Product product);

    ProductDto update(Long id,ProductDto dto);

    String deleteById(Long id);

}
