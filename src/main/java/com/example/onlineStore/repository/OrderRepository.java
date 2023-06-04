package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByIdAndRdtIsNull(Long id);

    List<Order> findAllByRdtIsNull();
    Order findByUserId(Long id);
    Order findByUserIdAndRdtIsNull(Long id);

    @Query(value = "select orders.sum from orders join user_table ut on orders.user_id = ut.id\n" +
            "          join payment p on p.id = orders.payment_id\n" +
            "where orders.user_id = ? and status = 'PENDING'",nativeQuery = true)
    Double getOrderSumByUSerId(Long userId);
}
