package io.muffin.cartservice.mapper;

import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.model.dto.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartMapper {

    public CartResponseDTO mapToCartResponseDTO(Cart cart, Page<CartItem> cartItems) {
        CartResponseDTO cartResponseDTO = CartResponseDTO.builder().build();
        cartResponseDTO.setCartId(cart.getId());

        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        cartItems.stream().forEach(ci -> {
            CartItemDTO cartItemDTO = CartItemDTO
                    .builder()
                    .cartItemId(ci.getId())
                    .price(ci.getPrice())
                    .productId(ci.getProductId())
                    .quantity(ci.getQuantity())
                    .build();
            cartItemDTOS.add(cartItemDTO);
        });

        cartResponseDTO.setCartItems(cartItemDTOS);
        return cartResponseDTO;
    }

    public CartItem mapToCartItem(CartItemDTO cartItemDTO, CartItem cartItem) {
        cartItem.setProductId(cartItemDTO.getProductId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(cartItemDTO.getPrice());
        return cartItem;
    }
}
