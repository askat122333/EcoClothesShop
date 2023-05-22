package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByIdAndRdtIsNull(Long id);

    List<Order> findAllByRdtIsNull();
}
