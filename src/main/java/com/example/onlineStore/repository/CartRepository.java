package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Cart;
import com.example.onlineStore.entity.User;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByIdAndRdtIsNull(Long id);

    List<Cart> findAllByRdtIsNull();
    //TODO
   Cart findByUserAndRdtIsNull(User user);
}
