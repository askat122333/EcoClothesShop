package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.repository.CategoryRepository;
import com.example.onlineStore.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private CategoryDto mapToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getProducts()
        );
    }

    @Override
    public CategoryDto getById(Long id) {
       Optional<Category> category = Optional.of(categoryRepository.findById(id).orElse(new Category()));
            return mapToDto(category.get());
    }

    @Override
    public Category getByIdEntity(Long id) {
        Category category = categoryRepository.findById(id).get();
        return category;
    }

    @Override
    public List<CategoryDto> getAll() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category:categoryList ) {
            categoryDtoList.add(mapToDto(category));
        }
        return categoryDtoList;
    }

    @Override
    public CategoryDto create(Category category) {
       return mapToDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Category category) {
        return mapToDto(categoryRepository.save(category));
    }

    @Override
    public String deleteById(Long id) {
        return null;
    }
}
