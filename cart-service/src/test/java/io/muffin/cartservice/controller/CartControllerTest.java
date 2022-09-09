package io.muffin.cartservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.model.dto.CartResponseDTO;
import io.muffin.cartservice.service.CartService;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testGetUserCart() throws Exception {
        CartResponseDTO cartResponseDTO = getCartResponseDTO();
        when(jwtUtil.extractTokenFromHeader(anyString())).thenReturn("the-token");
        when(cartService.getUserCart(anyString(), any(Pageable.class))).thenReturn(cartResponseDTO);
        mvc.perform(get("/cart").header("authorization", "some-token"))
                .andExpect(content().json(objectMapper.writeValueAsString(cartResponseDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testAddToCart() throws Exception {
        CartItemDTO cartItemDTO = getCartItemDTO();
        when(jwtUtil.extractTokenFromHeader(anyString())).thenReturn("the-token");
        when(cartService.addToCart(anyString(), any(CartItemDTO.class))).thenReturn(1L);
        mvc.perform(post("/cart/add")
                        .header("authorization", "some-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemDTO)))
                .andExpect(content().json("1"))
                .andExpect(status().isOk());

    }

    @Test
    void testEditCart() throws Exception {
        CartItemDTO cartItemDTO = getCartItemDTO();
        when(jwtUtil.extractTokenFromHeader(anyString())).thenReturn("the-token");
        when(cartService.editCart(anyString(), any(CartItemDTO.class))).thenReturn(1L);
        String loginJson = "{\"email\":\"user@email.com\",\"password\":\"password\"}";
        mvc.perform(put("/cart/edit")
                        .header("authorization", "some-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemDTO)))
                .andExpect(content().json("1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCart() throws Exception {
        when(jwtUtil.extractTokenFromHeader(anyString())).thenReturn("the-token");
        when(cartService.deleteCart(anyString(), eq(1L))).thenReturn(1L);
        mvc.perform(delete("/cart/delete/1")
                        .header("authorization", "some-token"))
                .andExpect(content().json("1"))
                .andExpect(status().isOk());
    }

    private CartResponseDTO getCartResponseDTO() {
        return CartResponseDTO.builder().build();
    }

    private CartItemDTO getCartItemDTO() {
        return CartItemDTO.builder()
                .cartItemId(1).build();
    }

}
