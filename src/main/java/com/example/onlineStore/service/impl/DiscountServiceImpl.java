package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.DiscountDto;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.exceptions.DiscountNotFoundException;
import com.example.onlineStore.repository.DiscountRepository;
import com.example.onlineStore.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    private DiscountDto mapToDto(Discount discount) {
        return DiscountDto.builder()
                .id(discount.getId())
                .name(discount.getName())
                .discount(discount.getDiscount())
                .rdt(discount.getRdt()).build();
    }

    @Override
    public DiscountDto getById(Long id) throws DiscountNotFoundException {
        try {
            Discount discount = discountRepository.findByIdAndRdtIsNull(id);
            return mapToDto(discount);
        } catch (NullPointerException e) {
            throw new DiscountNotFoundException("Скидка с id " + id + " не найдена.");
        }
    }

    @Override
    public Discount getByIdEntity(Long id) {
        return discountRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<DiscountDto> getAll() throws DiscountNotFoundException {
        List<Discount> discountList = discountRepository.findAllByRdtIsNull();
        if (discountList.isEmpty()) {
            throw new DiscountNotFoundException("В базе нет скидок.");
        }
        List<DiscountDto> discountDtoList = new ArrayList<>();
        for (Discount discount : discountList) {
            discountDtoList.add(mapToDto(discount));
        }
        return discountDtoList;
    }

    @Override
    public DiscountDto create(Discount discount) {
        return mapToDto(discountRepository.save(discount));
    }

    @Override
    public DiscountDto update(Long id, DiscountDto dto) throws DiscountNotFoundException {
        Discount discount = getByIdEntity(id);
        if (discount == null) {
            throw new DiscountNotFoundException("Скидка с id " + id + " не найдена.");
        }
        if (dto.getName() != null) {
            discount.setName(dto.getName());
        }
        if (dto.getDiscount() != null) {
            discount.setDiscount(dto.getDiscount());
        }
        return mapToDto(discountRepository.save(discount));
    }

    @Override
    public String deleteById(Long id) throws DiscountNotFoundException {
        try {
            Discount discount = getByIdEntity(id);
            discount.setRdt(LocalDate.now());
            discountRepository.save(discount);
            return "Скидка с id: " + id + " была удалена.";
        } catch (NullPointerException e) {
            throw new DiscountNotFoundException("Скидка с id " + id + " не найдена.");
        }

    }
}
