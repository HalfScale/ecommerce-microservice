package io.muffin.cartservice.repository;

import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    private Cart cart;

    @BeforeEach
    void beforeEach() {
        cart = Cart.builder()
                .id(null)
                .customerId(1L).build();
        cartRepository.save(cart);

        CartItem cartItemOne = CartItem.builder().id(null)
                .productId(1L)
                .cart(cart)
                .quantity(1)
                .price(BigDecimal.TEN)
                .build();
        CartItem cartItemTwo = CartItem.builder().id(null)
                .productId(2L)
                .cart(cart)
                .quantity(2)
                .price(BigDecimal.TEN).build();
        cartItemRepository.save(cartItemOne);
        cartItemRepository.save(cartItemTwo);
    }

    @AfterEach
    void afterEach() {
        cartItemRepository.deleteAll();
        cartItemRepository.flush();
    }

    @Test
    void test_findAllByCart() {
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);
        Assertions.assertTrue(!cartItems.isEmpty());
    }

    @Test
    void test_findAllpagedByCart() {
        Pageable pageable = Pageable.ofSize(10);
        Page<CartItem> cartItems = cartItemRepository.findAllPagedByCart(pageable, cart);
        Assertions.assertTrue(!cartItems.isEmpty());
    }
}
