package io.muffin.cartservice.repository;

import io.muffin.cartservice.model.Cart;
import io.muffin.cartservice.model.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart cart);
    Page<CartItem> findAllPagedByCart(Pageable pageable, Cart car);
}
