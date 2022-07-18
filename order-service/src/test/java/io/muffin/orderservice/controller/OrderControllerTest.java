package io.muffin.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.orderservice.model.dto.OrderRequestDTO;
import io.muffin.orderservice.model.dto.OrderResponseDTO;
import io.muffin.orderservice.service.OrdersService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdersController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrdersService ordersService;

    @Test
    void testGetOrder() throws Exception {
        OrderResponseDTO orderResponseDTO = getOrderResponseDTO();
        doReturn(orderResponseDTO).when(ordersService).getOrder(anyLong());
        mvc.perform(get("/order/1").header("authorization", "some-token"))
                .andExpect(content().json(objectMapper.writeValueAsString(orderResponseDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrder_ThrowsError() throws Exception {
        OrderResponseDTO orderResponseDTO = getOrderResponseDTO();
        doThrow(EcommerceException.class).when(ordersService).getOrder(anyLong());
        mvc.perform(get("/order").header("authorization", "some-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPlaceOrder() throws Exception {
        OrderRequestDTO orderRequestDTO = getOrderRequestDTO();
        when(ordersService.checkoutOrder(any(OrderRequestDTO.class), any())).thenReturn("success");
        mvc.perform(post("/order/add").header("authorization", "some-token")
                .content(objectMapper.writeValueAsString(orderRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("success"))
                .andExpect(status().isOk());
    }

    @Test
    void cancelOrder() throws Exception {
        when(ordersService.cancelOrder(anyLong())).thenReturn(1L);
        mvc.perform(put("/order/cancel/1").header("authorization", "some-token"))
                .andExpect(content().string("1"))
                .andExpect(status().isOk());
    }

    private OrderResponseDTO getOrderResponseDTO() {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        return orderResponseDTO;
    }

    private OrderRequestDTO getOrderRequestDTO() {
        return new OrderRequestDTO();
    }
}
