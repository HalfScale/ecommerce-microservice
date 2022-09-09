package io.muffin.cartservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.cartservice.mapper.CartMapper;
import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.model.dto.CartResponseDTO;
import io.muffin.cartservice.repository.CartItemRepository;
import io.muffin.cartservice.repository.CartRepository;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.ecommercecommons.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private SystemUtils systemUtils;

    @InjectMocks
    private CartService cartService;

    @Test
    void test_getUserCart() {
        Page<CartItem> cartItems = getCartItems();
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractId(any())).thenReturn(1L);
        when(cartRepository.findByCustomerId(anyLong())).thenReturn(Optional.of(getCart()));
        when(cartItemRepository.findAllPagedByCart(any(Pageable.class), any(Cart.class))).thenReturn(cartItems);
        when(cartMapper.mapToCartResponseDTO(any(Cart.class), eq(cartItems))).thenReturn(getCartResponseDTO());
        assertNotNull(cartService.getUserCart("some-jwt-token",Pageable.ofSize(1)));
    }

    @Test
    void test_getUserCart_throwsException_CartNotExisting() {
        Page<CartItem> cartItems = getCartItems();
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractId(any())).thenReturn(1L);
        when(cartRepository.findByCustomerId(anyLong())).thenReturn(Optional.empty());
        assertThrows(EcommerceException.class, () -> cartService.getUserCart("some-jwt-token",Pageable.ofSize(1)),
                "Cart not existing!");
    }

    @Test
    void test_addToCart_noCart() throws Exception {
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractId(anyString())).thenReturn(1L);
        when(cartRepository.findByCustomerId(anyLong())).thenReturn(Optional.empty());
        when(cartMapper.mapToCartItem(any(CartItemDTO.class),  any(CartItem.class))).thenReturn(getCartItem());
        when(cartRepository.save(any(Cart.class))).thenReturn(getCart());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(getCartItem());
        assertNotNull(cartService.addToCart("some-token", getCartItemDTO()));
    }

    @Test
    void test_addToCart_existingCart() throws Exception {
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractId(anyString())).thenReturn(1L);
        when(cartRepository.findByCustomerId(anyLong())).thenReturn(Optional.of(getCart()));
        when(cartMapper.mapToCartItem(any(CartItemDTO.class), any(CartItem.class))).thenReturn(getCartItem());
        when(objectMapper.writeValueAsString(any(CartItem.class))).thenReturn("objectToString");
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(getCartItem());
        assertNotNull(cartService.addToCart("some-token", getCartItemDTO()));
    }

    @Test
    void test_editCart() {
        CartItem cartItem = getCartItem();
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(cartMapper.mapToCartItem(any(CartItemDTO.class), eq(cartItem))).thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        assertNotNull(cartService.editCart("some-token", getCartItemDTO()));
    }

    @Test
    void test_editCart_throwsException_cartItemNotFound() {
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EcommerceException.class, () -> cartService.editCart("some-token", getCartItemDTO()),
                "Cart Item not found!");
    }

    @Test
    void test_deleteCart() {
        CartItem cartItem = getCartItem();
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).deleteById(anyLong());
        assertEquals(cartItem.getId(), cartService.deleteCart("some-token", cartItem.getId()));
    }

    @Test
    void test_deleteCart_throwsException_cartItemNotFound() {
        CartItem cartItem = getCartItem();
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EcommerceException.class, () -> cartService.deleteCart("some-token", cartItem.getId()),
                "Cart Item not found!");
    }

    private Cart getCart() {
        return Cart.builder().build();
    }

    private CartItemDTO getCartItemDTO() {
        return CartItemDTO.builder().build();
    }

    private Page<CartItem> getCartItems() {
        return new PageImpl(Arrays.asList(getCartItem()));
    }

    private CartItem getCartItem() {
        return CartItem.builder()
                .id(1L).build();
    }

    private CartResponseDTO getCartResponseDTO() {
        return CartResponseDTO.builder().build();
    }
}
