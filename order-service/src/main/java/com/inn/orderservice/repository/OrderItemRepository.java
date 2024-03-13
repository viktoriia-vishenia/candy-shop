package com.inn.orderservice.repository;

import com.inn.orderservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List <OrderItem> findAllByOrderId(Long id);
}
