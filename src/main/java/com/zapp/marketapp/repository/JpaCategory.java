package com.zapp.marketapp.repository;

import com.zapp.marketapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaCategory extends JpaRepository<Category,Integer> {
    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findAllByState(boolean state);
    Optional<Category> findByCategoryIdAndState(Integer categoryId,boolean state);

    List<Category> findByNameContainingIgnoreCaseAndState(String name,boolean state);
}
