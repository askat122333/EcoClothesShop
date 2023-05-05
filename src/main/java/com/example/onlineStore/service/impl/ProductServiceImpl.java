package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private ProductDto mapToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getPhoto(),
                product.getSize(),
                product.getMaterial(),
                product.getCategory()
        );
    }

    @Override
    public ProductDto getById(Long id) {
        Optional<Product> product = Optional.of(productRepository.findById(id).orElse(new Product()));
        return mapToDto(product.get());
    }

    @Override
    public Product getByIdEntity(Long id) {
        Product product = productRepository.findById(id).get();
        return product;
    }

    @Override
    public List<ProductDto> getAll() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product:productList) {
            productDtoList.add(mapToDto(product));
        }
        return productDtoList;
    }

    @Override
    public ProductDto create(Product product) {
        return mapToDto(productRepository.save(product));
    }

    @Override
    public ProductDto update(Product product) {
        return mapToDto(productRepository.save(product));
    }

    @Override
    public String deleteById(Long id) {
        return null;
    }
}
