package com.example.onlineStore.service;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {
    CategoryDto getById(Long id);

    Category getByIdEntity(Long id);

    List<CategoryDto> getAll();

    CategoryDto create(Category category);

    CategoryDto update(Category category);

    String deleteById(Long id);

}
