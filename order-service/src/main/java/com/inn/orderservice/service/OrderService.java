package com.inn.orderservice.service;

import com.inn.orderservice.dto.InventoryDto;
import com.inn.orderservice.dto.OrderDto;
import com.inn.orderservice.dto.OrderItemDto;
import com.inn.orderservice.model.Order;
import com.inn.orderservice.model.OrderItem;
import com.inn.orderservice.repository.OrderItemRepository;
import com.inn.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final WebClient.Builder webClientBuilder;


    public String addOrder(OrderDto orderDto) {

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = orderDto.getOrderItemDtos()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderItems(orderItems);

       List <String> invCodes = order.getOrderItems().stream().map(OrderItem::getInvCode).toList();

      InventoryDto[] inventoryAvailable = webClientBuilder.build().get()
                        .uri("http://inventory-service/inventory",
                                uriBuilder -> uriBuilder.queryParam("invCode", invCodes).build())
                                .retrieve()
                                        .bodyToMono(InventoryDto[].class)
                                                .block();

      boolean allProductsAreAvailable =  Arrays.stream(inventoryAvailable).allMatch(InventoryDto::isAvailable);

      if(allProductsAreAvailable){
          orderRepository.save(order);

          for (OrderItem orderItem : orderItems) {
              orderItem.setOrder(order);
              orderItemRepository.save(orderItem);
          }
      } else {
          throw new RuntimeException("Product is not available now");
      }
      return "Order added";
    }

    private OrderItem mapToDto(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setInvCode(orderItemDto.getInvCode());
        return orderItem;
    }



}
//    public void placeOrder(OrderDto orderDto) {
//        Order order = new Order();
//        order.setOrderNumber(UUID.randomUUID().toString());
//        List<OrderItem> orderItems = new ArrayList<>();
//
//        for (OrderItemDto orderItemDto : orderDto.getOrderItemDtos()) {
//            String message = "CHECK_INVENTORY:" + orderItemDto.getInvCode();
//            String response = (String) rabbitTemplate.convertSendAndReceive(topicExchange, @Value("${rabbitmq.routing.key}"), message);
//            if ("AVAILABLE".equals(response)) {
//                orderItems.add(mapToDto(orderItemDto));
//            } else {
//                System.out.println("Item not available");
//            }
//        }
//        order.setOrderItems(orderItems);
//        orderRepository.save(order);
//        orderItemRepository.save(orderItems);
//    }

