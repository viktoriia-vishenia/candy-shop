package com.inn.orderservice.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class OrderDto {

    private Long id;
    private String orderNumber;
    private List<OrderItemDto> orderItemDtos;
}