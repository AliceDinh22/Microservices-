package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryDTO;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {
  private final InventoryService inventoryService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
    return inventoryService.isInStock(skuCode, quantity);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void saveInventory(@RequestBody InventoryDTO inventoryDTO){
    inventoryService.saveInventory(inventoryDTO);
  }
}
