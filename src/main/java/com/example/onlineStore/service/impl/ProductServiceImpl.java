package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

                product.getSize(),
                product.getMaterial(),
                product.getCategory()
        );
    }

    @Override
    public ProductDto getById(Long id) throws ProductNotFoundException {
        try {
            Product product = productRepository.findByIdAndRdtIsNull(id);
            return mapToDto(product);
        }catch (NullPointerException e){
            throw new ProductNotFoundException("Продукт с таким id не найден.");
        }


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
    public ProductDto update(Long id,ProductDto dto) throws ProductNotFoundException {
        try {
            Product  product = getByIdEntity(id);

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

        return mapToDto(productRepository.save(product));
        }catch (NullPointerException e){
            throw  new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }
    }

    @Override
    public String deleteById(Long id) throws ProductNotFoundException {
        try{
            Product product = getByIdEntity(id);
            product.setRdt(LocalDate.now());
            productRepository.save(product);
            return "Продукт с id: "+id+" был удален.";
        }catch (NullPointerException e){
            throw new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }

    }

    //TODO
    @Override
    public List<ProductDto> getAllByCategory(Long categoryId) {
        List<Product> productList = productRepository.findAllByCategoryAndRdtIsNull(categoryId);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product:productList) {
            productDtoList.add(mapToDto(product));
        }
        return productDtoList;
    }



    public String uploadImage(Long productId, MultipartFile file) throws ProductNotFoundException {
        try {

            Product product = getByIdEntity(productId);
            try {
                product.setImage(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            productRepository.save(product);
            return "Image saved to database successfully!";
        }catch (NullPointerException e){
            throw new ProductNotFoundException("Продукт с id "+productId+" не найден.");
        }
    }


    public  byte[] getImageById(Long id) {
        Product product = getByIdEntity(id);
        return product.getImage();
    }
}
