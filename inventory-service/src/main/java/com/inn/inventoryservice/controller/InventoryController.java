package com.inn.inventoryservice.controller;

import com.inn.inventoryservice.dto.InventoryDto;
import com.inn.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> isAvailable(@RequestParam List<String> invCode){
      return inventoryService.isAvailable(invCode);
    }
}
