package com.inn.inventoryservice.service;

import com.inn.inventoryservice.dto.InventoryDto;
import com.inn.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryDto> isAvailable(List<String> invCode) {
        return inventoryRepository.findByInvCodeIn(invCode).stream()
                .map(inventory -> InventoryDto.builder()
                            .invCode(inventory.getInvCode())
                            .isAvailable(inventory.getQuantity() > 0).build()
                ).toList();
    }
}
