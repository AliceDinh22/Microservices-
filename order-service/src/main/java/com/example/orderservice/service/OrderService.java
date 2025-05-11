package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.event.OrderPlacedEvent;
import com.example.orderservice.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;
  private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

  public void saveOrder(OrderDTO orderDTO) {
    var isProductInStock = inventoryClient.isInStock(orderDTO.skuCode(), orderDTO.quantity());

    if (isProductInStock) {
      Order order = new Order();
      order.setOrderNumber(UUID.randomUUID().toString());
      order.setEmail(orderDTO.userDetails().email());
      order.setSkuCode(orderDTO.skuCode());
      order.setName(orderDTO.name());
      order.setPrice(orderDTO.price());
      order.setQuantity(orderDTO.quantity());
      orderRepository.save(order);

      OrderPlacedEvent orderPlacedEvent=new OrderPlacedEvent();
      orderPlacedEvent.setOrderNumber(order.getOrderNumber());
      orderPlacedEvent.setEmail(orderDTO.userDetails().email());
      orderPlacedEvent.setFirstName(orderDTO.userDetails().firstName());
      orderPlacedEvent.setLastName(orderDTO.userDetails().lastName());
      log.info("Start- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);
      kafkaTemplate.send("order-placed", orderPlacedEvent);
      log.info("End- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);
    } else {
      throw new RuntimeException("Product " + orderDTO.name() + " is not in stock");
    }
  }
}
