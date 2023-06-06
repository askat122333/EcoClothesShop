package com.example.onlineStore.controller;

import com.example.onlineStore.dto.MvcDto.ProductMvcDto;
import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.dto.SearchDto;
import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.enums.Size;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import ognl.ListPropertyAccessor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.*;
import java.util.List;
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable @Min(1) Long id) throws ProductNotFoundException {
        return productService.getById(id);
    }

    @GetMapping("/all")
    public List<ProductDto> getAll() throws ProductNotFoundException {
        return productService.getAll();
    }

    @GetMapping("/allWithImage")
    public List<ProductMvcDto> getAllMvc() throws ProductNotFoundException {
        return productService.getAllMvc();
    }
    @GetMapping("/search")
    public List<ProductDto> search(@RequestBody SearchDto searchDto){
       return productService.dynamicSearch(searchDto);
    }

    @GetMapping("/getNew")
    public List<ProductDto> getAllByType() throws ProductNotFoundException{
        return productService.getAllByType();
    }

    @PostMapping("/create")
    public ProductDto addNewProduct(@RequestBody ProductDto dto){
        return productService.create(dto);
    }

    @PutMapping("/update")
    public ProductDto updateProduct(@RequestParam("productId")@Min(1) Long id,
                                    @RequestBody ProductDto dto
                                    /*@RequestParam("discountId")@Min(1) Long discountId,
                                    @RequestParam("categoryId")@Min(1) Long categoryId*/) throws ProductNotFoundException {
        return productService.update(id,dto);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getAllByCategory(@PathVariable Long categoryId) throws ProductNotFoundException {
        return productService.getAllByCategory(categoryId);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws ProductNotFoundException {
        return productService.deleteById(id);
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("productId") Long productId,
            @RequestParam("imageFile") MultipartFile file) throws IOException, ProductNotFoundException {
    return productService.uploadImage(productId, file);
    }



    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageById(@PathVariable("id") Long id) throws Exception {
        return productService.getImageById(id);
    }

    @PutMapping("/addCategory")
    public ProductDto addCategory(@RequestParam("productId") Long productId,
                                  @RequestParam("categoryId") Long categoryId){
        return productService.addCategory(productId,categoryId);
    }

    @PutMapping("/addDiscount")
    public ProductDto addDiscount(@RequestParam("productId") Long productId,
                                  @RequestParam("discountId")Long discountId){
        return productService.addDiscount(productId,discountId);
    }
}
