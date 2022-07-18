package io.muffin.cartservice.repository;

import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Test
    void testFindAllByCart() {
        Cart cart = new Cart(null, 1L);
        cartRepository.save(cart);
        CartItem cartItemOne = new CartItem(null, 1L, cart, 1, BigDecimal.TEN);
        CartItem cartItemTwo = new CartItem(null, 2L, cart, 2, BigDecimal.TEN);
        cartItemRepository.save(cartItemOne);
        cartItemRepository.save(cartItemTwo);

        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        Assertions.assertTrue(!cartItems.isEmpty());
    }
}
