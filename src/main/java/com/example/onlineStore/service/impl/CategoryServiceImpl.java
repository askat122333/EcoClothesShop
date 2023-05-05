package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.CategoryRepository;
import com.example.onlineStore.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
       Category category = categoryRepository.findByIdAndRdtIsNull(id);
            return mapToDto(category);
    }

    @Override
    public Category getByIdEntity(Long id) {
        return categoryRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<CategoryDto> getAll() {
        List<Category> categoryList = categoryRepository.findAllByRdtIsNull();
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
    public CategoryDto update(Long id,CategoryDto dto) {
        Category category = getByIdEntity(id);
        if(dto.getName()!=null){
            category.setName(dto.getName());
        }
        if(dto.getProducts()!=null){
            category.setProducts(dto.getProducts());
        }
        return mapToDto(categoryRepository.save(category));
    }

    @Override
    public String deleteById(Long id) {
        Category category = getByIdEntity(id);
        category.setRdt(LocalDate.now());
        categoryRepository.save(category);
        return "Категория с id: "+id+" была удалена.";
    }
}
