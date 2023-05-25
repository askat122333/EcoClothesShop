package com.example.onlineStore.service.impl;

import com.example.onlineStore.entity.Product;
import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
@EnableScheduling
public class JobService {
    private final ProductRepository productRepository;
    @Scheduled(cron = "0 0 12 * * ?")
    public void checkProductType() {
        List<Product> products = productRepository.findAllByRdtIsNull();
        for (Product product : products) {
            if (product.getDateAdded().plusDays(20).isBefore(LocalDate.now()) ||
                    product.getDateAdded().plusDays(20).isEqual(LocalDate.now())) {
                product.setProductType(ProductType.REGULAR);
                productRepository.save(product);
            } else if (product.getDateAdded().isAfter(LocalDate.now()) &&
                    product.getDateAdded().isBefore(product.getDateAdded().plusDays(1))) {
                product.setProductType(ProductType.REGULAR);
                productRepository.save(product);
            }
        }
    }
/*    @Scheduled(fixedRate = 5000)
    public void checkProductType2(){
        List<Product> products = productRepository.findAllByRdtIsNull();
        for (Product product:products) {
            if (product.getDateAdded().plusDays(1).isBefore(LocalDate.now()) ||
                    product.getDateAdded().plusDays(1).isEqual(LocalDate.now()) ) {
                product.setProductType(ProductType.REGULAR);
                productRepository.save(product);
            }else if (product.getDateAdded().isAfter(LocalDate.now()) &&
                    product.getDateAdded().isBefore(product.getDateAdded().plusDays(1)) ){
                product.setProductType(ProductType.REGULAR);
                productRepository.save(product);
            }
        }
    }*/

}
