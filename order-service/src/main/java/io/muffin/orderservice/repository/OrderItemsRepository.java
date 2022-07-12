package io.muffin.orderservice.repository;

import io.muffin.orderservice.model.OrderItems;
import io.muffin.orderservice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    Optional<List<OrderItems>> findAllByOrderId(Orders orders);
}
