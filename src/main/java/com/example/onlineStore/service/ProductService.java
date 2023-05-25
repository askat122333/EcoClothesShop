package com.example.onlineStore.service;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface ProductService {
    ProductDto getById(Long id) throws ProductNotFoundException;

    Product getByIdEntity(Long id);

    List<ProductDto> getAll() throws ProductNotFoundException;

    ProductDto create(ProductDto dto);

    ProductDto update(Long id,ProductDto dto) throws ProductNotFoundException;

    String deleteById(Long id) throws ProductNotFoundException;

    List<ProductDto> getAllByCategory(Long categoryId) throws ProductNotFoundException;

    String uploadImage(Long productId, MultipartFile file) throws IOException, ProductNotFoundException;
    byte[] getImageById(Long id) throws Exception;
    List<ProductDto> getAllByType() throws ProductNotFoundException;
}
