package com.zapp.marketapp.repository;

import com.zapp.marketapp.entities.Order;
import com.zapp.marketapp.utils.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaOrder extends JpaRepository<Order, OrderId> {
    Optional<Order> findByOrderIdAndState(OrderId orderId, boolean state);

    @Query("SELECT product.price * quantity FROM Order")
    List<Long> total();

    List<Order> findAllByOrderIdUserId(Integer userId);
}
