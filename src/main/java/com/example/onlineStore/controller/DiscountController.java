package com.example.onlineStore.controller;

import com.example.onlineStore.dto.DiscountDto;
import com.example.onlineStore.exceptions.DiscountNotFoundException;
import com.example.onlineStore.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public DiscountDto getById(@PathVariable @Min(1) Long id)throws DiscountNotFoundException {
        return discountService.getById(id);
    }
    @GetMapping("/all")
    public List<DiscountDto> getAll()throws DiscountNotFoundException{
        return discountService.getAll();
    }
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:update')")
    public DiscountDto addNewDiscount(@RequestBody DiscountDto dto){
        return discountService.create(dto);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public DiscountDto update(@PathVariable Long id ,
                              @RequestBody DiscountDto dto)throws DiscountNotFoundException{
        return discountService.update(id,dto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public String delete(@PathVariable Long id)throws DiscountNotFoundException{
        return discountService.deleteById(id);
    }
}
