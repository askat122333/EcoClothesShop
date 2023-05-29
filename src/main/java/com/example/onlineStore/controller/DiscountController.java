package com.example.onlineStore.controller;

import com.example.onlineStore.dto.DiscountDto;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.exceptions.DiscountNotFoundException;
import com.example.onlineStore.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;
    @GetMapping("/{id}")
    public DiscountDto getById(@PathVariable @Min(1) Long id)throws DiscountNotFoundException {
        return discountService.getById(id);
    }
    @GetMapping("/all")
    public List<DiscountDto> getAll()throws DiscountNotFoundException{
        return discountService.getAll();
    }
    @PostMapping("/create")
    public DiscountDto addNewDiscount(@RequestBody DiscountDto dto){
        return discountService.create(dto);
    }

    @PutMapping("/update/{id}")
    public DiscountDto update(@PathVariable Long id ,
                              @RequestBody DiscountDto dto)throws DiscountNotFoundException{
        return discountService.update(id,dto);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id)throws DiscountNotFoundException{
        return discountService.deleteById(id);
    }
}
