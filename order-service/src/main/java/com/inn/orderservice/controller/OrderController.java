package com.inn.orderservice.controller;

import com.inn.orderservice.dto.OrderDto;
import com.inn.orderservice.dto.OrderItemDto;
import com.inn.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addOrder(@RequestBody OrderDto orderDto){
        orderService.addOrder(orderDto);
        return "Order added to the queue";
    }

    @DeleteMapping("/{orderNumber}/item/{invCodeOrderItem}")
    public String deleteItemFromOrder(@PathVariable String orderNumber, @PathVariable String invCodeOrderItem){
        orderService.deleteItemFromOrder(orderNumber, invCodeOrderItem);
        return "Candy deleted from the order";
    }
}
