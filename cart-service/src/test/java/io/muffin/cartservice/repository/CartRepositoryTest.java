package io.muffin.cartservice.repository;

import io.muffin.cartservice.model.Cart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    void test_addCart() {
        Cart cart = Cart.builder()
                .id(null)
                .customerId(1L)
                .build();
        cartRepository.save(cart);
        cartRepository.findByCustomerId(1L).get();
        Assertions.assertEquals(cartRepository.findByCustomerId(1L).get(), cart);
    }
}
