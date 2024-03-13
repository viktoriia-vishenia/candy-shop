package com.inn.orderservice.controller;

import com.inn.orderservice.dto.OrderDto;
import com.inn.orderservice.dto.OrderItemDto;
import com.inn.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public List<OrderDto> findAll(){
     return orderService.findAll();
    }

    @GetMapping("/find/{orderNumber}")
    public OrderDto findOrder(@PathVariable String orderNumber) {
        return orderService.getByOrderNumber(orderNumber);
    }
    @PatchMapping("/update-status/{orderNumber}")
    public void updateOrderStatus(@RequestBody String statusDto,
                                  @PathVariable String orderNumber)  {
        orderService.updateOrderStatus(statusDto, orderNumber);
    }

    @PatchMapping("/update-order-items/{orderNumber}")
    public void updateOrderItems(@RequestBody List <OrderItemDto> orderItemDto,
                                  @PathVariable String orderNumber)  {
        orderService.updateOrderItems(orderItemDto, orderNumber);
    }

    @PostMapping("/add")
    public void addOrder(@RequestBody List <OrderItemDto> orderItemDtos)  {
       orderService.addOrder(orderItemDtos);
    }

    @DeleteMapping("/delete/{orderNumber}")
    public void deleteOrder(@PathVariable String orderNumber) {
        orderService.deleteOrder(orderNumber);
    }
}