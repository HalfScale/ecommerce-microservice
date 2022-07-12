package io.muffin.orderservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderDTO {

    private Long id;
    private Long customerId;
    private BigDecimal amount;
    private String shippingAddress;
    private List<OrderItemsDTO> orderItems;
}
