package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.DiscountDto;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.exceptions.DiscountNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.DiscountRepository;
import com.example.onlineStore.service.DiscountService;
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
            log.error("Метод getById(Discount), Exception: Скидка с id " + id + " не найдена.");
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
            log.error("Метод getAll(Discount), Exception: В базе нет скидок.");
            throw new DiscountNotFoundException("В базе нет скидок.");
        }
        List<DiscountDto> discountDtoList = new ArrayList<>();
        for (Discount discount : discountList) {
            discountDtoList.add(mapToDto(discount));
        }
        return discountDtoList;
    }

    @Override
    public DiscountDto create(@Valid DiscountDto dto) {
        Discount discount = Discount.builder()
                .name(dto.getName())
                .discount(dto.getDiscount())
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Discount>> violations = validator.validate(discount);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Discount> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод create(Discount): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(discountRepository.save(discount));
    }

    @Transactional
    @Override
    public DiscountDto update(Long id,@Valid DiscountDto dto) throws DiscountNotFoundException {
        Discount discount = getByIdEntity(id);
        if (discount == null) {
            log.error("Метод update(Discount), Exception: Скидка с id " + id + " не найдена.");
            throw new DiscountNotFoundException("Скидка с id " + id + " не найдена.");
        }
        if (dto.getName() != null) {
            discount.setName(dto.getName());
        }
        if (dto.getDiscount() != null) {
            discount.setDiscount(dto.getDiscount());
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Discount>> violations = validator.validate(discount);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Discount> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(Discount): "+errorMessages);
            }
            throw new ValidException(errorMessages);
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
            log.error("Метод deleteById(Discount), Exception: Скидка с id " + id + " не найдена.");
            throw new DiscountNotFoundException("Скидка с id " + id + " не найдена.");
        }

    }
}
