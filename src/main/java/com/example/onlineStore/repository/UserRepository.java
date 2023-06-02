package com.example.onlineStore.repository;

import com.example.onlineStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByIdAndRdtIsNull(Long id);

    List<User> findAllByRdtIsNull();
    User findByEmail(String email);

    User findByLogin(String login);

}
