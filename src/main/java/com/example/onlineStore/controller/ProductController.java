package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ErrMessage;
import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.dto.ProductDtoWithImageResponse;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.service.ProductService;
import com.sun.xml.bind.v2.TODO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
//    @GetMapping(value = "/{id}")
//    public ProductDto getById(@PathVariable Long id) throws IOException {
////        ProductDto productDto = productService.getById(id);
////        byte[] byteArray = productDto.getImage();
////        String base64Encoded = Base64.getEncoder().encodeToString(byteArray);
////
////        byte[] decodedBytes = Base64.getDecoder().decode(base64Encoded);
////        String decodedString = new String(decodedBytes);
////        productDto.setImg(decodedString);
//        ProductDto productDto = productService.getById(id);
//        byte[] byteArray = productDto.getImage();
//        String base64Encoded = Base64.getEncoder().encodeToString(byteArray);
//        String prefixedBase64 = "data:image/jpeg;base64," + base64Encoded;
//        productDto.setImg(prefixedBase64);
//        return productDto;
//
//    }
//    @GetMapping(value = "/{id}")
//        public ResponseEntity<?> getById(@PathVariable Long id) throws IOException, ProductNotFoundException {
//
//        try {
//            ProductDto  productDto = productService.getById(id);
//            return ResponseEntity.ok().body(productDto);
//
//        } catch (NullPointerException e) {
//            return new ResponseEntity<>(new ErrMessage(HttpStatus.NOT_FOUND.value(),
//                    "Продукт с таким id не найден."),HttpStatus.NOT_FOUND);
//
//    }
//    }
    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) throws ProductNotFoundException {
        return productService.getById(id);
    }

    @GetMapping("/all")
    public List<ProductDto> getAll() throws ProductNotFoundException {
        return productService.getAll();
    }
    @GetMapping("/getNew")
    public List<ProductDto> getAllByType() throws ProductNotFoundException{
        return productService.getAllByType();
    }

    @PostMapping("/create")
    public ProductDto addNewProduct(@RequestBody Product product){
        return productService.create(product);
    }

    @PutMapping("/update/{id}")
    public ProductDto updateProduct(@PathVariable Long id,
                                 @RequestBody ProductDto dto) throws ProductNotFoundException {
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
}
