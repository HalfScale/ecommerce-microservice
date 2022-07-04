package io.muffin.orderservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemsRequestDTO {

    private Long id;
    private Long productId;
    private BigDecimal price;
    private int quantity;
}
