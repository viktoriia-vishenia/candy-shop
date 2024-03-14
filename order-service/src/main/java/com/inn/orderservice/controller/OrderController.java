package com.inn.orderservice.controller;

import com.inn.orderservice.dto.OrderDto;
import com.inn.orderservice.dto.OrderItemDto;
import com.inn.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> updateOrderStatus(@RequestBody String statusDto,
                                            @PathVariable String orderNumber)  {
        orderService.updateOrderStatus(statusDto, orderNumber);
        return new ResponseEntity<>("Order's status updated successfully", HttpStatus.OK);
    }

    @PatchMapping("/update-order-items/{orderNumber}")
    public ResponseEntity<String> updateOrderItems(@RequestBody List <OrderItemDto> orderItemDto,
                                  @PathVariable String orderNumber)  {
        orderService.updateOrderItems(orderItemDto, orderNumber);
        return new ResponseEntity<>("Order compound was updated successfully.",HttpStatus.OK);
    }

    @PatchMapping("/send-order/{orderNumber}")
    public ResponseEntity<String> sendOrder( @PathVariable String orderNumber)  {
        orderService.sendOrder(orderNumber);
        return new ResponseEntity<>("Order was sent successfully.",HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody List <OrderItemDto> orderItemDtos)  {
       orderService.addOrder(orderItemDtos);
        return new ResponseEntity<>("Order was added successfully.",HttpStatus.OK);
    }

    @DeleteMapping("/delete/{orderNumber}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderNumber) {
        orderService.deleteOrder(orderNumber);
        return new ResponseEntity<>("Order was deleted successfully.",HttpStatus.OK);
    }
}