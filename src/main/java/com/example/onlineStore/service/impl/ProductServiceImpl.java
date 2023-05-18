package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                product.getCategory(),
                product.getRdt()
        );
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findByIdAndRdtIsNull(id);
        return mapToDto(product);
    }

    @Override
    public Product getByIdEntity(Long id) {
        return productRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<ProductDto> getAll() {
        List<Product> productList = productRepository.findAllByRdtIsNull();
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
    public ProductDto update(Long id,ProductDto dto) {
        Product product = getByIdEntity(id);
        if(dto.getName()!=null){
            product.setName(dto.getName());
        }
        if(dto.getPrice()!=null){
            product.setPrice(dto.getPrice());
        }
        if(dto.getSize()!=null){
            product.setSize(dto.getSize());
        }
        if(dto.getMaterial()!=null){
            product.setMaterial(dto.getMaterial());
        }
        if(dto.getCategory()!=null){
            product.setCategory(dto.getCategory());
        }
        if(dto.getPhoto()!=null){
            product.setPhoto(dto.getPhoto());
        }
        return mapToDto(productRepository.save(product));
    }

    @Override
    public String deleteById(Long id) {
        Product product = getByIdEntity(id);
        product.setRdt(LocalDate.now());
        productRepository.save(product);
        return "Продукт с id: "+id+" был удален.";
    }

    @Override
    public List<ProductDto> getAllByCategory(Long categoryId) {
        List<Product> productList = productRepository.findAllByCategoryAndRdtIsNull(categoryId);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product:productList) {
            productDtoList.add(mapToDto(product));
        }
        return productDtoList;
    }
}
