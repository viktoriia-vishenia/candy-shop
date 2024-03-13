package com.inn.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

        private Long id;
        private String invCode;
        private Double price;
        private Integer quantity;
        private OrderDto orderDto;

    }

