package io.muffin.cartservice.mapper;

import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.model.dto.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartMapper {

    public CartResponseDTO mapToCartResponseDTO(Cart cart, List<CartItem> cartItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartId(cart.getId());

        for (CartItem cartItem : cartItems) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            totalAmount = totalAmount.add(cartItem.getAmount());
            cartItemDTO.setCartItemId(cartItem.getId());
            cartItemDTO.setAmount(cartItem.getAmount());
            cartItemDTO.setProductId(cartItem.getProductId());
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTOS.add(cartItemDTO);
        }
        cartResponseDTO.setCartItems(cartItemDTOS);
        cartResponseDTO.setTotalAmount(totalAmount);
        return cartResponseDTO;
    }

    public CartItem mapToCartItem(CartItemDTO cartItemDTO, CartItem cartItem) {
        cartItem.setProductId(cartItemDTO.getProductId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setAmount(cartItemDTO.getAmount());
        return cartItem;
    }
}
