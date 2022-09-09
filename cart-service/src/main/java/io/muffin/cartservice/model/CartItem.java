package io.muffin.cartservice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private int quantity;
    private BigDecimal price;
}
