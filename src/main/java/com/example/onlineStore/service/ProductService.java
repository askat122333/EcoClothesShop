package com.example.onlineStore.service;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
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

    //TODO
    List<ProductDto> getAllByCategory(Long categoryId);
}
