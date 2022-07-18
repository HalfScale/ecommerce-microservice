package io.muffin.orderservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemsDTO {

    private Long id;
    private Long productId;
    private BigDecimal price;
    private int quantity;
}
