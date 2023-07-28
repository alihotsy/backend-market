package com.zapp.marketapp.repository;

import com.zapp.marketapp.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface JpaProduct extends JpaRepository<Product,Integer> {

    List<Product> findAllByState(boolean state, Pageable pageable);
    Optional<Product> findByProductIdAndState(Integer productId, boolean state);

    List<Product> findByNameContainingIgnoreCaseAndState(String name,boolean state);

}
