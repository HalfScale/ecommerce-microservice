package io.muffin.cartservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.model.dto.CartResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapperTest {

    private static CartMapper cartMapper;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        cartMapper = new CartMapper();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testMapToCartResponseDTO() throws JsonProcessingException {
        Cart cart = Cart.builder().id(1L).customerId(1L).build();
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .productId(1L)
                .cart(cart)
                .quantity(1)
                .price(new BigDecimal(1))
                .build();

        List<CartItem> cartItems = Arrays.asList(cartItem);
        List<CartItemDTO> cartItemDTOS = cartItems.stream()
                .map(c -> {
                    return CartItemDTO.builder()
                            .cartItemId(c.getId())
                            .price(c.getPrice())
                            .productId(c.getProductId())
                            .quantity(c.getQuantity())
                            .build();
                }).collect(Collectors.toList());

        Page cartItemsPaged = new PageImpl(cartItems);
        CartResponseDTO expectedObject = CartResponseDTO.builder()
                        .cartId(cart.getId()).cartItems(cartItemDTOS)
                        .build();

        assertThat(cartMapper.mapToCartResponseDTO(cart, cartItemsPaged)).usingRecursiveComparison()
                .isEqualTo(expectedObject);
    }

    @Test
    public void testMapToCartItem() {
        CartItemDTO cartItemDTO = CartItemDTO.builder()
                .productId(1L)
                .quantity(1)
                .price(new BigDecimal(1000))
                .build();

        CartItem cartItem = CartItem.builder()
                .productId(cartItemDTO.getProductId())
                .quantity(cartItemDTO.getQuantity())
                .price(cartItemDTO.getPrice())
                .build();

        assertThat(cartMapper.mapToCartItem(cartItemDTO, cartItem)).usingRecursiveComparison()
                .isEqualTo(cartItem);
    }
}
