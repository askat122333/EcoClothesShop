package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {
    Discount findByIdAndRdtIsNull(Long id);

    List<Discount> findAllByRdtIsNull();
}
