package com.inn.orderservice.controller;

import com.inn.orderservice.dto.OrderDto;
import com.inn.orderservice.dto.OrderItemDto;
import com.inn.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @CircuitBreaker(name = "inventoryUpdate", fallbackMethod = "fallbackSendOrder")
    @TimeLimiter(name = "inventoryUpdate")
    @Retry(name = "inventoryUpdate")
    public CompletableFuture<String> sendOrder( @PathVariable String orderNumber)  {
        return CompletableFuture.supplyAsync(() -> orderService.sendOrder(orderNumber));
    }

    @PostMapping("/add")
    @CircuitBreaker(name = "inventoryIsAvailable", fallbackMethod = "fallbackAddOrder")
    @TimeLimiter(name = "inventoryIsAvailable")
    @Retry(name = "inventoryIsAvailable")
    public CompletableFuture<String> addOrder(@RequestBody List <OrderItemDto> orderItemDtos)  {
        return CompletableFuture.supplyAsync(() -> orderService.addOrder(orderItemDtos));
    }

    @DeleteMapping("/delete/{orderNumber}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderNumber) {
        orderService.deleteOrder(orderNumber);
        return new ResponseEntity<>("Order was deleted successfully.",HttpStatus.OK);
    }

    public CompletableFuture<String> fallBackAddOrder(List<OrderItemDto> orderItemDtos, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Something went wrong, please order after some time!");
    }

    public CompletableFuture<String> fallBackSendOrder(String orderNumber, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Something went wrong, please send after some time!");
    }
}