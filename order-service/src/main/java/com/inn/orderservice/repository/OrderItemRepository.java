package com.inn.orderservice.repository;

import com.inn.orderservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT o FROM OrderItem o WHERE LOWER(o.invCode) LIKE LOWER(concat('%', :invCode, '%'))")
    Optional<OrderItem> findByInvCode(String invCode);
}
