package com.inn.orderservice.repository;


import com.inn.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderByOrderNumber(String orderNumber);

    Order getOrderByOrderNumber(String orderNumber);
    List<Order> findAll ();

    void deleteOrderByOrderNumber(String orderNumber);
}
