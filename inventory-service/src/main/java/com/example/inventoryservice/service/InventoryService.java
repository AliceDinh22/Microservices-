package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryDTO;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  public boolean isInStock(String skuCode, Integer quantity) {
    return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
  }

  public void saveInventory(InventoryDTO inventoryDTO) {
    Inventory inventory=new Inventory();
    inventory.setSkuCode(inventoryDTO.skuCode());
    inventory.setName(inventoryDTO.name());
    inventory.setQuantity(inventoryDTO.quantity());
    inventoryRepository.save(inventory);
  }

}
