package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.exceptions.CategoryNotFoundException;
import com.example.onlineStore.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) throws CategoryNotFoundException {
        return categoryService.getById(id);
    }

    @GetMapping("/all")
    public List<CategoryDto> getAll() throws CategoryNotFoundException {
        return categoryService.getAll();
    }

    @PostMapping("/create")
    public CategoryDto addNewCategory(@RequestBody Category category){
        return categoryService.create(category);
    }

    @PutMapping("/update/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                @RequestBody CategoryDto dto) throws CategoryNotFoundException {
        return categoryService.update(id,dto);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws CategoryNotFoundException {
        return categoryService.deleteById(id);
    }
}
