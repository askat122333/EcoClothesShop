package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByIdAndRdtIsNull(Long id);

    List<Category> findAllByRdtIsNull();
}
