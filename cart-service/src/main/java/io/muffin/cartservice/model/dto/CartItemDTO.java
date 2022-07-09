package io.muffin.cartservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {

    private long cartItemId;
    private long productId;
    private int quantity;
    private BigDecimal amount;

}
