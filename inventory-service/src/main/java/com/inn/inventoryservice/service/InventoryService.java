package com.inn.inventoryservice.service;

import com.inn.inventoryservice.dto.InventoryDto;
import com.inn.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryDto> isAvailable(List<String> invCode) {
        log.info("Searching by invCodes");
        return inventoryRepository.findByInvCodeIn(invCode).stream()
                .map(inventory -> InventoryDto.builder()
                            .invCode(inventory.getInvCode())
                            .isAvailable(inventory.getQuantity() > 0).build()
                ).toList();
    }
}
