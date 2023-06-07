package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.exceptions.CategoryNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.CategoryRepository;
import com.example.onlineStore.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private CategoryDto mapToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getProducts(),
                category.getRdt()
                );
    }

    @Override
    public CategoryDto getById(Long id) throws CategoryNotFoundException {
        try {
            Category category = categoryRepository.findByIdAndRdtIsNull(id);
            return mapToDto(category);
        }catch (NullPointerException e){
            log.error("Метод getById(Category), Exception: Категория с id "+id+" не найдена.");
            throw new CategoryNotFoundException("Категория с id "+id+" не найдена.");
        }

    }

    @Override
    public Category getByIdEntity(Long id) {
        return categoryRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<CategoryDto> getAll() throws CategoryNotFoundException {
        List<Category> categoryList = categoryRepository.findAllByRdtIsNull();
        if(categoryList.isEmpty()){
            log.error("Метод getAll(Category), Exception: В базе нет категорий.");
            throw new CategoryNotFoundException("В базе нет категорий.");
        }
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category:categoryList ) {
            categoryDtoList.add(mapToDto(category));
        }
        return categoryDtoList;
    }

    @Override
    public CategoryDto create(@Valid CategoryDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .products(dto.getProducts())
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Category> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод create(Category): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }
       return mapToDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto update(Long id,@Valid CategoryDto dto) throws CategoryNotFoundException {
        Category category = getByIdEntity(id);
        if(category==null){
            log.error("Метод update(Category), Exception: Категория с id "+id+" не найдена.");
            throw new CategoryNotFoundException("Категория с id "+id+" не найдена.");
        }
        if(dto.getName()!=null){
            category.setName(dto.getName());
        }
        if(dto.getProducts()!=null){
            category.setProducts(dto.getProducts());
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Category> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(Category): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(categoryRepository.save(category));
    }

    @Override
    public String deleteById(Long id) throws CategoryNotFoundException {
        try {
            Category category = getByIdEntity(id);
            category.setRdt(LocalDate.now());
            categoryRepository.save(category);
            return "Категория с id: "+id+" была удалена.";
        }catch (NullPointerException e){
            log.error("Метод deleteById(Category), Exception: Категория с id "+id+" не найдена.");
            throw new CategoryNotFoundException("Категория с id "+id+" не найдена.");
        }

    }
}
