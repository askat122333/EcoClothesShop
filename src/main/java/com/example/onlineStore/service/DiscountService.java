package com.example.onlineStore.service;

import com.example.onlineStore.dto.DiscountDto;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.exceptions.DiscountNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiscountService {
    DiscountDto getById(Long id) throws DiscountNotFoundException;

    Discount getByIdEntity(Long id);

    List<DiscountDto> getAll() throws DiscountNotFoundException;

    DiscountDto create(DiscountDto dto);

    DiscountDto update(Long id,DiscountDto dto) throws DiscountNotFoundException;

    String deleteById(Long id) throws DiscountNotFoundException;
}
