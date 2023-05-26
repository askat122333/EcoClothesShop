package com.example.onlineStore.service;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.exceptions.CategoryNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {
    CategoryDto getById(Long id) throws CategoryNotFoundException;

    Category getByIdEntity(Long id);

    List<CategoryDto> getAll() throws CategoryNotFoundException;

    CategoryDto create(CategoryDto dto);

    CategoryDto update(Long id,CategoryDto dto) throws CategoryNotFoundException;

    String deleteById(Long id) throws CategoryNotFoundException;

}
