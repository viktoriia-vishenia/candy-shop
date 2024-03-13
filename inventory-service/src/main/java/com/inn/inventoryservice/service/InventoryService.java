package com.inn.inventoryservice.service;

import com.inn.inventoryservice.dto.InventoryDto;
import com.inn.inventoryservice.model.Inventory;
import com.inn.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    public List<InventoryDto> isAvailable(List<String> invCode) {

     return  inventoryRepository.findByInvCodeIn(invCode).stream()
                .map(inventory ->
                        InventoryDto.builder()
                                .invCode(inventory.getInvCode())
                                .quantity(inventory.getQuantity())
                                .isAvailable(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }

    @Transactional
    public void updateInventory(InventoryDto inventoryDto) {

        Inventory inventory = inventoryRepository
                .findInventoryByInvCode(inventoryDto.getInvCode());

        Integer newQuantity = inventory.getQuantity() - inventoryDto.getQuantity();

        if (newQuantity > 0) {
            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
        } else {
            inventoryRepository.deleteInventoryByInvCode(inventory.getInvCode());
        }
    }

    @Transactional(readOnly = true)
    public List<InventoryDto> getAll() {

        List<Inventory> inventories = inventoryRepository.findAll();

        return inventories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addInventory(InventoryDto inventoryDto) {
        Inventory inventory = new Inventory();
        inventory.setInvCode(inventoryDto.getInvCode());
        inventory.setQuantity(inventoryDto.getQuantity());
        inventoryRepository.save(inventory);
    }

    private InventoryDto convertToDto(Inventory inventory) {
        InventoryDto dto = new InventoryDto();
        dto.setInvCode(inventory.getInvCode());
        dto.setQuantity(inventory.getQuantity());
        return dto;
    }
}



