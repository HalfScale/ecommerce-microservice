package io.muffin.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.feign.InventoryFeignClient;
import io.muffin.ecommercecommons.model.dto.ProductRequestDTO;
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
import java.util.ArrayList;
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
        // will call the inventory feign client for update of product stocks - decrease the stocks
        // on the inventory service
        List<ProductResponseDTO> productResponseDTOS = validateOrderProducts(orderRequestDTO);
        String token = jwtUtil.extractTokenFromHeader(auth);
        long userId = jwtUtil.extractId(token);

        Orders order = modelMapper.map(orderRequestDTO, Orders.class);
        order.setCustomerId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Constants.ORDER_STATUS_PLACED);
        log.info("ORDER_PERSISTED => [{}]", objectMapper.writeValueAsString(order));
        ordersRepository.save(order);

        for (OrderItemsDTO orderItem : orderRequestDTO.getOrderItems()) {
            // for each item here, subtract it from the current stock
            // call the inventory service for this.
            OrderItems item = modelMapper.map(orderItem, OrderItems.class);
            item.setOrderId(order);
            log.info("ORDER_ITEM_PERSISTED => [{}]", objectMapper.writeValueAsString(item));
            orderItemsRepository.save(item);
        }

        // update (decrease the stocks) each items that is present in an order
        productResponseDTOS.forEach(product -> {
            ProductRequestDTO productRequestDTO = modelMapper.map(product, ProductRequestDTO.class);
            inventoryFeignClient.updateProduct(productRequestDTO);
        });
        return "Order Placed Successfully!";
    }

    public OrderResponseDTO getOrder(Long orderId) {
        // causing multiple, should only get order by id
        Orders orders = ordersRepository.findById(orderId)
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

        List<OrderItems> orderItems = orderItemsRepository.findAllByOrderId(orders)
                .orElseThrow(() -> new EcommerceException("Order items not existing!"));

        // query and update the product, to increment its stock
        // since cancelling of order, will return the quantity that is placed recently.
        orderItems.forEach(items -> {
            ProductResponseDTO productResponseDTO = inventoryFeignClient.findProductById(items.getProductId());
            ProductRequestDTO productRequestDTO = modelMapper.map(productResponseDTO, ProductRequestDTO.class);
            int previousStock = productResponseDTO.getStock();
            int newStock = previousStock + items.getQuantity();
            productRequestDTO.setStock(newStock);
            inventoryFeignClient.updateProduct(productRequestDTO);
        });

        ordersRepository.save(orders);
        return orders.getId();
    }

    private List<ProductResponseDTO> validateOrderProducts(OrderRequestDTO orderRequestDTO) {
        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();
        orderRequestDTO.getOrderItems().forEach(item -> {
            ProductResponseDTO productResponseDTO = inventoryFeignClient.findProductById(item.getProductId());
            if (!Objects.isNull(productResponseDTO)) {
                int stock = productResponseDTO.getStock();
                // if the request for quantity is greater than the stock then throw error.
                if (stock <= 0 || item.getQuantity() > stock) {
                    throw new EcommerceException("Product not in stock!");
                }
                // return an updated quantity of product stocks
                // to be used for updating
                productResponseDTO.setStock(stock - item.getQuantity());
                productResponseDTOS.add(productResponseDTO);
            }
        });
        return productResponseDTOS;
    }
}
