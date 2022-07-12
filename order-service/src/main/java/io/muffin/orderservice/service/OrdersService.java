package io.muffin.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.feign.InventoryFeignClient;
import io.muffin.ecommercecommons.model.dto.ProductResponseDTO;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.orderservice.model.OrderItems;
import io.muffin.orderservice.model.Orders;
import io.muffin.orderservice.model.dto.OrderItemsDTO;
import io.muffin.orderservice.model.dto.OrderRequestDTO;
import io.muffin.orderservice.model.dto.OrderResponseDTO;
import io.muffin.orderservice.repository.OrderItemsRepository;
import io.muffin.orderservice.repository.OrdersRepository;
import io.muffin.orderservice.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final InventoryFeignClient inventoryFeignClient;

    public String checkoutOrder(OrderRequestDTO orderRequestDTO, String auth) throws JsonProcessingException {
        validateOrderProducts(orderRequestDTO);
        String token = jwtUtil.extractTokenFromHeader(auth);
        long userId = jwtUtil.extractId(token);

        Orders order = modelMapper.map(orderRequestDTO, Orders.class);
        order.setCustomerId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Constants.ORDER_STATUS_PLACED);
        log.info("ORDER_TO_BE_PERSISTED => [{}]", objectMapper.writeValueAsString(order));
        ordersRepository.save(order);

        for (OrderItemsDTO orderItem : orderRequestDTO.getOrderItems()) {
            OrderItems item = modelMapper.map(orderItem, OrderItems.class);
            item.setOrderId(order);
            log.info("ORDER_ITEM_PERSISTED => [{}]", objectMapper.writeValueAsString(item));
            orderItemsRepository.save(item);
        }

        return "Order Placed Successfully!";
    }

    public OrderResponseDTO getOrder(String auth) {
        String token = jwtUtil.extractTokenFromHeader(auth);
        long userId = jwtUtil.extractId(token);

        Orders orders = ordersRepository.findByCustomerId(userId)
                .orElseThrow(() -> new EcommerceException("Order not existing!"));

        List<OrderItems> orderItems = orderItemsRepository.findAllByOrderId(orders)
                .orElseThrow(() -> new EcommerceException("Order items not existing!"));

        List<OrderItemsDTO> orderItemDTOS = orderItems.stream()
                .map(item -> modelMapper.map(item, OrderItemsDTO.class))
                .collect(Collectors.toList());

        OrderResponseDTO orderResponseDTO = modelMapper.map(orders, OrderResponseDTO.class);
        orderResponseDTO.setOrderItems(orderItemDTOS);

        return orderResponseDTO;
    }

    public long cancelOrder(long orderId) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new EcommerceException("Order not existing!"));
        orders.setStatus(Constants.ORDER_STATUS_CANCELLED);
        ordersRepository.save(orders);
        return orders.getId();
    }

    private void validateOrderProducts(OrderRequestDTO orderRequestDTO) {
        orderRequestDTO.getOrderItems().forEach(item -> {
            ProductResponseDTO productResponseDTO = inventoryFeignClient.findProductById(item.getProductId());
            if(!Objects.isNull(productResponseDTO)) {
                int stock = productResponseDTO.getStock();
                if(stock <= 0) {
                    throw new EcommerceException("Product not in stock!");
                }
            }
        });
    }
}
