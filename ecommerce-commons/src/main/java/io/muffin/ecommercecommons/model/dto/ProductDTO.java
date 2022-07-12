package io.muffin.ecommercecommons.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public abstract class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private int stock;
    private BigDecimal price;
}
