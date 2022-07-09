package io.muffin.cartservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartResponseDTO {

    private long cartId;
    private List<CartItemDTO> cartItems;
    private BigDecimal totalAmount;

}
