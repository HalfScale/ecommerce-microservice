package io.muffin.inventoryservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private int stock;
    private BigDecimal price;
}
