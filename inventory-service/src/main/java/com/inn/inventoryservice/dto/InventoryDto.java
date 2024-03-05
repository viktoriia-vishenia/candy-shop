package com.inn.inventoryservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDto {
        private String invCode;
        private boolean isAvailable;
}
