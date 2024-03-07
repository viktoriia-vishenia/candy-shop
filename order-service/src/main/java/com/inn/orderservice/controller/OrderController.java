package com.inn.orderservice.controller;

import com.inn.orderservice.dto.OrderDto;
import com.inn.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> addOrder(@RequestBody OrderDto orderDto){
        log.info("Adding of order in process");
        return CompletableFuture.supplyAsync(() -> orderService.addOrder(orderDto));
    }

    public CompletableFuture<String> fallbackMethod(OrderDto orderDto, RuntimeException runtimeException) {
        log.info("Order haven't added, fallback method executed");
        return CompletableFuture.supplyAsync(()
                -> " Something went wrong, please order after some time!");
    }
}
