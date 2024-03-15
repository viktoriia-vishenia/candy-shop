package com.inn.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDto {

        private String invCode;
        private Integer quantity;
        private boolean isAvailable;
        private String orderNumber;
}
