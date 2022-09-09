package io.muffin.cartservice.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CartItemDTO {

    private long cartItemId;
    private long productId;
    private int quantity;
    private BigDecimal price;

}
