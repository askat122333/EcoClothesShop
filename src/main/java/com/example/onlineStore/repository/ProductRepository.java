package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByIdAndRdtIsNull(Long id);

    List<Product> findAllByRdtIsNull();
    //TODO
    @Query(value = "select * from product where category_id = ?1", nativeQuery = true)
    List<Product> findAllByCategoryAndRdtIsNull(Long categoryId);
}
