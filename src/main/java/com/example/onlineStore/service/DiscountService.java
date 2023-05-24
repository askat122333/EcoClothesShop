package com.example.onlineStore.service;

import com.example.onlineStore.dto.DiscountDto;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.exceptions.CategoryNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiscountService {
    DiscountDto getById(Long id) throws CategoryNotFoundException;

    Discount getByIdEntity(Long id);

    List<DiscountDto> getAll() throws CategoryNotFoundException;

    DiscountDto create(Discount discount);

    DiscountDto update(Long id,DiscountDto dto) throws CategoryNotFoundException;

    String deleteById(Long id) throws CategoryNotFoundException;
}
