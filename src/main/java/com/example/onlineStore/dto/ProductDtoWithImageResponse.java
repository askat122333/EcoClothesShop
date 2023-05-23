package com.example.onlineStore.dto;

public class ProductDtoWithImageResponse {
    private ProductDto productDto;
    private byte[] imageData;

    // Constructor, getters, and setters
    // ...

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}