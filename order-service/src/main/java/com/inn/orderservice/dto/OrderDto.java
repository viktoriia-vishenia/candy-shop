package com.inn.orderservice.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class OrderDto {

    private String orderNumber;
    private String orderStatus;
    private List<OrderItemDto> orderItemDtos;
}