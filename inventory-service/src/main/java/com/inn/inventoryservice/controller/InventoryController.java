package com.inn.inventoryservice.controller;

import com.inn.inventoryservice.dto.InventoryDto;
import com.inn.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> getAll(){
     return  inventoryService.getAll();
    }

    @GetMapping("/available")
    public List<InventoryDto> isAvailable(@RequestParam List<String> invCode){
      return inventoryService.isAvailable(invCode);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateInventory(@RequestBody List <InventoryDto> inventoryDtoList){
        inventoryService.updateInventory(inventoryDtoList);
        return new ResponseEntity<>("Inventory updated successfully", HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addInventory(@RequestBody InventoryDto inventory) {
        inventoryService.addInventory(inventory);
        return new ResponseEntity<>("Inventory added successfully", HttpStatus.CREATED);
    }
}
