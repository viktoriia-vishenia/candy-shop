package com.inn.orderservice.service;

import com.inn.orderservice.dto.InventoryDto;
import com.inn.orderservice.dto.OrderDto;
import com.inn.orderservice.dto.OrderItemDto;
import com.inn.orderservice.model.Order;
import com.inn.orderservice.model.OrderItem;
import com.inn.orderservice.model.Status;
import com.inn.orderservice.repository.OrderItemRepository;
import com.inn.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final WebClient.Builder webClientBuilder;


    @Transactional
    public String addOrder(List <OrderItemDto> orderItemDtos) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderStatus(Status.INITIATED);

        List<OrderItem> requiredOrderItems = orderItemDtos.stream()
                .map(this::mapToOrderItem).toList();

        List<String> invCodes = requiredOrderItems.stream()
                .map(OrderItem::getInvCode)
                .toList();

        InventoryDto[] inventoryAvailable = webClientBuilder.build().get()
                .uri("http://localhost:8089/inventory/available",
                        uriBuilder -> uriBuilder.queryParam("invCode", invCodes).build())
                .retrieve()
                .bodyToMono(InventoryDto[].class)
                .block();


            Map<String, Integer> map = Arrays.stream(inventoryAvailable)
                    .collect(Collectors.toMap(InventoryDto::getInvCode, InventoryDto::getQuantity));

            List<OrderItem> itemsInOrder = new ArrayList<>();

            for (OrderItem item : requiredOrderItems) {
                String invCode = item.getInvCode();
                if (map.get(invCode) >= item.getQuantity()) {
                    itemsInOrder.add(item);
                }
            }

            if (!itemsInOrder.isEmpty()) {
                order.setOrderItems(itemsInOrder);
                for (OrderItem orderItem : itemsInOrder) {
                    orderItem.setOrder(order);
                }
                order.setOrderStatus(Status.CREATED);
                orderRepository.save(order);
            } else {
                order.setOrderStatus(Status.REJECTED);
                orderRepository.save(order);
            }
            return "Order is added with status " + order.getOrderStatus();
        }

    @Transactional
    public void updateOrderStatus(String statusDto, String orderNumber) {
            Order order = orderRepository
                    .findOrderByOrderNumber(orderNumber);

            if (order != null) {
               order.setOrderStatus(Status.fromString(statusDto));
               orderRepository.save(order);
            } else {
                throw new RuntimeException("Order is not found");
            }
        }

        @Transactional
        public String sendOrder(String orderNumber){
        Order order = orderRepository
                    .findOrderByOrderNumber(orderNumber);
        if (order == null){
            throw new RuntimeException("Order is not found");
        }

        Status status = order.getOrderStatus();
        if(status.equals(Status.REJECTED)) {
            throw new RuntimeException("Order couldn't be sent. Something went wrong");
        } else if (status.equals(Status.SENT)){
            throw new RuntimeException("Order already sent");
        } else if (status.equals(Status.INITIATED)) {
            throw new RuntimeException("Wait please, order hasn't created");
        }

        List<OrderItem> orderItems = orderItemRepository
                .findAllByOrderId(order.getId());

      List<InventoryDto> inventoryDtoList =
             orderItems.stream().map(this::mapToInventoryDto).toList();

        ResponseEntity<Void> responseEntity = webClientBuilder.build().patch()
                    .uri("http://localhost:8089/inventory/update")
                    .body(BodyInserters.fromValue(inventoryDtoList))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

          if(responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()){
              order.setOrderStatus(Status.SENT);
              orderRepository.save(order);
              return "Order is sent";
          } else{
              throw new RuntimeException("Sorry, order couldn't be sent, something went wrong");
          }
        }

        @Transactional
        public void updateOrderItems(List <OrderItemDto> orderItemsDto
                , String orderNumber) {

            Order order = orderRepository
                    .getOrderByOrderNumber(orderNumber);

            if (order != null) {

                List<OrderItem> oldOrderItems =
                        orderItemRepository.findAllByOrderId(order.getId());

                orderItemRepository.deleteAll(oldOrderItems);

                List <OrderItem> newOrderItems = orderItemsDto
                        .stream().map(this::mapToOrderItem).collect(Collectors.toList());

                for (OrderItem orderItem: newOrderItems) {
                    orderItem.setOrder(order);
                }
                order.setOrderItems(newOrderItems);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order is not found");
        }
    }

        @Transactional
        public void deleteOrder(String orderNumber) {
            Order order = orderRepository.getOrderByOrderNumber(orderNumber);
            Status orderStatus = order.getOrderStatus();
            if (!orderStatus.equals(Status.CREATED)) {
                List<OrderItem> orderItems =
                        orderItemRepository.findAllByOrderId(order.getId());
                orderItemRepository.deleteAll(orderItems);

                orderRepository.deleteOrderByOrderNumber(orderNumber);
            } else {
                throw new RuntimeException("Order already created");
            }
        }

       @Transactional(readOnly = true)
       public List<OrderDto> findAll(){
      List<Order> orders = orderRepository.findAll();
      return orders.stream().map(this::mapToOrderDto).toList();
       }

       @Transactional(readOnly = true)
       public OrderDto getByOrderNumber(String orderNumber){
       Order order = orderRepository.getOrderByOrderNumber(orderNumber);
       return mapToOrderDto(order);
       }

    private OrderItem mapToOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setInvCode(orderItemDto.getInvCode());
        return orderItem;
    }
    private OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setInvCode(orderItem.getInvCode());
        return orderItemDto;
    }

    private OrderDto mapToOrderDto (Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setOrderStatus(order.getOrderStatus().toString());
        orderDto.setOrderItemDtos(order.getOrderItems()
                .stream().map(this::mapToOrderItemDto).toList());
        return orderDto;
    }

    private InventoryDto mapToInventoryDto (OrderItem orderItem){
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setInvCode(orderItem.getInvCode());
        inventoryDto.setQuantity(orderItem.getQuantity());

        return inventoryDto;
    }
}


