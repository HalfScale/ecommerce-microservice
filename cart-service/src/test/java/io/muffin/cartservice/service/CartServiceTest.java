package io.muffin.cartservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.cartservice.mapper.CartMapper;
import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.model.dto.CartResponseDTO;
import io.muffin.cartservice.repository.CartItemRepository;
import io.muffin.cartservice.repository.CartRepository;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.feign.RestAuthConsumer;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.ecommercecommons.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private RestAuthConsumer restAuthConsumer;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private SystemUtils systemUtils;

    @InjectMocks
    private CartService cartService;

    @Test
    void testGetUserCart() {
        List<CartItem> cartItems = getCartItems();
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractId(any())).thenReturn(1L);
        when(cartRepository.findByCustomerId(anyLong())).thenReturn(Optional.of(getCart()));
        when(cartItemRepository.findAllByCart(any(Cart.class))).thenReturn(cartItems);
        when(cartMapper.mapToCartResponseDTO(any(Cart.class), eq(cartItems))).thenReturn(getCartResponseDTO());
        assertNotNull(cartService.getUserCart("some-jwt-token"));
    }

    @Test
    void testAddToCart_CreatingNewCart() throws Exception {
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractId(anyString())).thenReturn(1L);
        when(cartRepository.findByCustomerId(anyLong())).thenReturn(Optional.empty());
        when(cartMapper.mapToCartItem(any(CartItemDTO.class),  any(CartItem.class))).thenReturn(getCartItem());
        when(cartRepository.save(any(Cart.class))).thenReturn(getCart());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(getCartItem());
        assertNotNull(cartService.addToCart("some-token", getCartItemDTO()));
    }

    @Test
    void testAddToCart_ExistingCart() throws Exception {
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractId(anyString())).thenReturn(1L);
        when(cartRepository.findByCustomerId(anyLong())).thenReturn(Optional.of(getCart()));
        when(cartMapper.mapToCartItem(any(CartItemDTO.class), any(CartItem.class))).thenReturn(getCartItem());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(getCartItem());
        assertNotNull(cartService.addToCart("some-token", getCartItemDTO()));
    }

    @Test
    void editCart_Success() {
        CartItem cartItem = getCartItem();
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(cartMapper.mapToCartItem(any(CartItemDTO.class), eq(cartItem))).thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        assertNotNull(cartService.editCart("some-token", getCartItemDTO()));
    }

    @Test
    void editCart_ThrowsException() {
        when(systemUtils.validateToken(anyString())).thenReturn(true);
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EcommerceException.class, () -> cartService.editCart("some-token", getCartItemDTO()),
                "Cart Item not found!");
    }

    private Cart getCart() {
        Cart cart = new Cart();
        return cart;
    }

    private CartItemDTO getCartItemDTO() {
        return new CartItemDTO();
    }

    private List<CartItem> getCartItems() {
        return Arrays.asList(getCartItem());
    }

    private CartItem getCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        return cartItem;
    }

    private CartResponseDTO getCartResponseDTO() {
        return new CartResponseDTO();
    }
}
