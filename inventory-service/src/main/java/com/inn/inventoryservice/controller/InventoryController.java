package com.inn.inventoryservice.controller;

import com.inn.inventoryservice.dto.InventoryDto;
import com.inn.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> isAvailable(@RequestParam List<String> invCode){
        log.info("Check availability of products by invCodes: {}", invCode);
      return inventoryService.isAvailable(invCode);
    }
}
