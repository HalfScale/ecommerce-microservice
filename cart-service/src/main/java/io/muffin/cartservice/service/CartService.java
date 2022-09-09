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
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.ecommercecommons.util.SystemUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final SystemUtils systemUtils;
//    private final KafkaSenderService kafkaSenderService;


    public CartResponseDTO getUserCart(String token, Pageable pageable) {
        systemUtils.validateToken(token);
        long userId = jwtUtil.extractId(token);
        Cart cart = cartRepository.findByCustomerId(userId)
                .orElseThrow(() -> new EcommerceException("Cart not existing!"));
        Page<CartItem> cartItems = cartItemRepository.findAllPagedByCart(pageable, cart);
        return cartMapper.mapToCartResponseDTO(cart, cartItems);
    }

    public long addToCart(String token, CartItemDTO cartItemDTO) throws JsonProcessingException {
        systemUtils.validateToken(token);
        long userId = jwtUtil.extractId(token);
        Cart cart = cartRepository.findByCustomerId(userId)
                .orElse(null);
        CartItem cartItem = cartMapper.mapToCartItem(cartItemDTO, new CartItem());

        if (cart != null) {
            cartItem.setCart(cart);
            log.info("ADDED_NEW_CART_ITEM => [{}]", objectMapper.writeValueAsString(cartItem));
            cartItemRepository.save(cartItem);
        } else {
            Cart newCart = new Cart();
            newCart.setCustomerId(userId);
            cartRepository.save(newCart);
            cartItem.setCart(newCart);
            cartItemRepository.save(cartItem);
        }
        return cartItem.getId();
    }

    public long editCart(String token, CartItemDTO cartItemDTO) {
        systemUtils.validateToken(token);
        CartItem cartItem = cartItemRepository.findById(cartItemDTO.getCartItemId())
                .orElseThrow(() -> new EcommerceException("Cart Item not found!"));

        CartItem updatedCartItem = cartMapper.mapToCartItem(cartItemDTO, cartItem);
        cartItemRepository.save(updatedCartItem);
        return cartItem.getId();
    }

    public long deleteCart(String token, long cartItemId) {
        systemUtils.validateToken(token);
        cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EcommerceException("Cart Item not found!"));
        cartItemRepository.deleteById(cartItemId);
        return cartItemId;
    }


}
