package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.DiscountDto;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.exceptions.CategoryNotFoundException;
import com.example.onlineStore.repository.DiscountRepository;
import com.example.onlineStore.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    @Override
    public DiscountDto getById(Long id) throws CategoryNotFoundException {
        return null;
    }

    @Override
    public Discount getByIdEntity(Long id) {
        return null;
    }

    @Override
    public List<DiscountDto> getAll() throws CategoryNotFoundException {
        return null;
    }

    @Override
    public DiscountDto create(Discount discount) {
        return null;
    }

    @Override
    public DiscountDto update(Long id, DiscountDto dto) throws CategoryNotFoundException {
        return null;
    }

    @Override
    public String deleteById(Long id) throws CategoryNotFoundException {
        return null;
    }
}
