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

  /*  @Query(value = "select * from orders join Payment on Payment.id = orders.payment_id " +
            "where orders.user_id = ? and Payment.status ='PENDING' ")*/
/*    Order findByUserAndPaymentStatus_Pending(Long userId);*/

}
