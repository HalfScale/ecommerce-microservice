package io.muffin.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.feign.InventoryFeignClient;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.ecommercecommons.model.dto.ProductResponseDTO;
import io.muffin.orderservice.model.OrderItems;
import io.muffin.orderservice.model.Orders;
import io.muffin.orderservice.model.dto.OrderItemsDTO;
import io.muffin.orderservice.model.dto.OrderRequestDTO;
import io.muffin.orderservice.model.dto.OrderResponseDTO;
import io.muffin.orderservice.repository.OrderItemsRepository;
import io.muffin.orderservice.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderItemsRepository orderItemsRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private InventoryFeignClient inventoryFeignClient;

    @InjectMocks
    private OrdersService ordersService;

    @Test
    void testCheckoutProduct() throws Exception {
        ProductResponseDTO productResponseDTO = getProductResponseDTO();
        productResponseDTO.setStock(1);
        when(inventoryFeignClient.findProductById(anyLong())).thenReturn(productResponseDTO);
        when(jwtUtil.extractTokenFromHeader(anyString())).thenReturn("token");
        when(jwtUtil.extractId(anyString())).thenReturn(1L);
        doReturn(getOrders()).when(modelMapper).map(any(OrderRequestDTO.class), eq(Orders.class));
        doReturn(getOrderItems()).when(modelMapper).map(any(OrderItemsDTO.class), eq(OrderItems.class));
        assertNotNull(ordersService.checkoutOrder(getOrderRequestDTO(), "auth"));
    }

    @Test
    void testCheckoutProduct_ThrowException() throws Exception {
        ProductResponseDTO productResponseDTO = getProductResponseDTO();
        productResponseDTO.setStock(0);
        when(inventoryFeignClient.findProductById(anyLong())).thenReturn(productResponseDTO);
        assertThrows(EcommerceException.class, () -> ordersService.checkoutOrder(getOrderRequestDTO(), "auth"),
                "Product not in stock!");

    }

    @Test
    void testGetOrder() {
        when(jwtUtil.extractTokenFromHeader(anyString())).thenReturn("token");
        when(jwtUtil.extractId(anyString())).thenReturn(1L);
        doReturn(Optional.of(getOrders())).when(ordersRepository).findByCustomerId(anyLong());
        doReturn(Optional.of(getOrderItemsList())).when(orderItemsRepository).findAllByOrderId(any(Orders.class));
        doReturn(getOrderItemsDTO()).when(modelMapper).map(any(OrderItems.class), eq(OrderItemsDTO.class));
        doReturn(getOrderResponseDTO()).when(modelMapper).map(any(Orders.class), eq(OrderResponseDTO.class));
        assertNotNull(ordersService.getOrder("some-token"));
    }

    @Test
    void testGetOrder_ThrowsException() {
        when(jwtUtil.extractTokenFromHeader(anyString())).thenReturn("token");
        when(jwtUtil.extractId(anyString())).thenReturn(1L);
        doReturn(Optional.empty()).when(ordersRepository).findByCustomerId(anyLong());
        assertThrows(EcommerceException.class, () -> ordersService.getOrder("some-token"),
                "Order not existing!");
    }

    @Test
    void testCancelOrder() {
        when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(getOrders()));
        when(ordersRepository.save(any(Orders.class))).thenReturn(getOrders());
        assertNotNull(ordersService.cancelOrder(1L));
    }

    @Test
    void testCancelOrder_ThrowsException() {
        when(ordersRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EcommerceException.class, () -> ordersService.cancelOrder(1L),
                "Order not existing!");
    }

    private ProductResponseDTO getProductResponseDTO() {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        return productResponseDTO;
    }

    private OrderRequestDTO getOrderRequestDTO() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setOrderItems(Arrays.asList(getOrderItemsDTO(),
                getOrderItemsDTO()));
        return orderRequestDTO;
    }

    private OrderItemsDTO getOrderItemsDTO() {
        OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
        orderItemsDTO.setProductId(1L);
        return orderItemsDTO;
    }

    private OrderResponseDTO getOrderResponseDTO() {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        return orderResponseDTO;
    }

    private List<OrderItemsDTO> getOrderItemDTOS() {
        OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
        orderItemsDTO.setProductId(1L);
        return Arrays.asList(orderItemsDTO, orderItemsDTO);
    }

    private Orders getOrders() {
        Orders orders = new Orders();
        orders.setId(1L);
        return orders;
    }

    private OrderItems getOrderItems() {
        OrderItems orderItems = new OrderItems();
        return orderItems;
    }

    private List<OrderItems> getOrderItemsList() {
        OrderItems orderItems = new OrderItems();
        return Arrays.asList(orderItems, orderItems);
    }


}
