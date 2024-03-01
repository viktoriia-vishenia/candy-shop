package com.inn.orderservice.repository;

import com.inn.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderNumber=:orderNumber")
    Optional<Order> findByOrderNumber(String orderNumber);


}
