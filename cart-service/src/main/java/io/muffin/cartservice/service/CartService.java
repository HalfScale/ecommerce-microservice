package io.muffin.cartservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.repository.CartItemRepository;
import io.muffin.cartservice.repository.CartRepository;
import io.muffin.ecommercecommons.feign.RestAuthConsumer;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import io.muffin.ecommercecommons.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RestAuthConsumer restAuthConsumer;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public String addToCart(String auth, CartItemDTO cartItemDTO) throws JsonProcessingException {
        String token = auth.replace("Bearer ", "");
        UserResponseDTO userResponseDTO = restAuthConsumer.validateEmail(jwtUtil.extractUsername(token));
        Cart cart = cartRepository.findByCustomerId(userResponseDTO.getId()).orElse(null);

        CartItem cartItem = modelMapper.map(cartItemDTO, CartItem.class);

        // should validate if the item that will be added to cart is still existing.

        if (cart != null) {
            cartItem.setCartId(cart);
            log.info("cart item to be saved => [{}]", objectMapper.writeValueAsString(cartItem));
            cartItemRepository.save(cartItem);
        } else {
            Cart newCart = new Cart();
            newCart.setCustomerId(userResponseDTO.getId());
            cartRepository.save(newCart);

            cartItem.setCartId(newCart);
            cartItemRepository.save(cartItem);
        }

        return "Item added to cart successfully!";
    }
}
