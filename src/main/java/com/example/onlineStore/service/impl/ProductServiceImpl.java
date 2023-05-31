package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .size(product.getSize())
                .price(product.getPrice())
                .material(product.getMaterial())
                .productType(product.getProductType())
                .dateAdded(product.getDateAdded())
                .discount(product.getDiscount())
                .category(product.getCategory())
                .build();
    }

    @Override
    public ProductDto getById(Long id) throws ProductNotFoundException {
        try {
            Product product = productRepository.findByIdAndRdtIsNull(id);
            return mapToDto(product);
        }catch (NullPointerException e){
            log.error("Метод getById(Product), Exception: Продукт с id "+id+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }


    }

    @Override
    public Product getByIdEntity(Long id) {
        return productRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<ProductDto> getAll() throws ProductNotFoundException {

            List<Product> productList = productRepository.findAllByRdtIsNull();
            if (productList.isEmpty()) {
                log.error("Метод getAll(Product), Exception: В базе нет товаров.");
                throw new ProductNotFoundException("В базе нет товаров.");
            }
            List<ProductDto> productDtoList = new ArrayList<>();
            for (Product product:productList) {
                productDtoList.add(mapToDto(product));
            }
            return productDtoList;

    }
@Override
public ProductDto create(@Valid ProductDto dto) {
    Product product = Product.builder()
            .name(dto.getName())
            .discount(dto.getDiscount())
            .category(dto.getCategory())
            .price(dto.getPrice())
            .dateAdded(LocalDate.now())
            .productType(ProductType.NEW)
            .material(dto.getMaterial())
            .size(dto.getSize())
            .build();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    if (!violations.isEmpty()) {

        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<Product> violation : violations) {
            errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
            log.warn("Метод create(Product): "+errorMessages);
        }
        throw new ValidException(errorMessages);
    }

    return mapToDto(productRepository.save(product));
}
    @Override
    public List<ProductDto> getAllByType() throws ProductNotFoundException {
        List<Product> productList = productRepository.findAllByProductTypeAAndRdtIsNull();
        if (productList.isEmpty()){
            log.error("Метод getAllByType(Product), Exception: В базе нет новинок.");
            throw new ProductNotFoundException("В базе нет новинок.");
        }
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product:productList) {
            productDtoList.add(mapToDto(product));
        }
        return productDtoList;
    }


    @Override
    @Transactional
    public ProductDto update(Long id,@Valid ProductDto dto) throws ProductNotFoundException {

            Product  product = getByIdEntity(id);
            if(product==null){
                log.error("Метод update(Product), Exception: Продукт с id "+id+" не найден.");
                throw  new ProductNotFoundException("Продукт с id "+id+" не найден.");
            }

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
        if (dto.getDiscount()!=null){
            product.setDiscount(dto.getDiscount());
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Product> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(Product): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(productRepository.save(product));
    }

    @Override
    public String deleteById(Long id) throws ProductNotFoundException {
        try{
            Product product = getByIdEntity(id);
            product.setRdt(LocalDate.now());
            productRepository.save(product);
            return "Продукт с id: "+id+" был удален.";
        }catch (NullPointerException e){
            log.error("Метод deleteById(Product), Exception: Продукт с id "+id+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }

    }

    @Override
    public List<ProductDto> getAllByCategory(Long categoryId) throws ProductNotFoundException {
        List<Product> productList = productRepository.findAllByCategoryAndRdtIsNull(categoryId);
        if (productList.isEmpty()) {
            log.error("Метод getAllByCategory(Product), Exception: В базе нет товаров данной категории.");
            throw new ProductNotFoundException("В базе нет товаров данной категории.");
        }
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
            log.error("Метод uploadImage(Product), Exception: Продукт с id "+productId+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+productId+" не найден.");
        }
    }


    public  byte[] getImageById(Long id) throws ProductNotFoundException {
        try {
            Product product = getByIdEntity(id);
            return product.getImage();
        }catch (NullPointerException e){
            log.error("Метод getImageById(Product), Exception: Продукт с id "+id+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }

    }
}
