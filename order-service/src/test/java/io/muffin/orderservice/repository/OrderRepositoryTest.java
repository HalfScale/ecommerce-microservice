package io.muffin.orderservice.repository;

import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.orderservice.model.Orders;
import io.muffin.orderservice.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    void testFindByCustomerId_OrderFound() {
        // given
        Orders orders = new Orders();
        orders.setId(1L);
        orders.setOrderDate(LocalDateTime.now());
        orders.setCustomerId(4L);
        orders.setAmount(BigDecimal.TEN);
        orders.setShippingAddress("shipping-address");
        orders.setStatus(Constants.ORDER_STATUS_PLACED);
        ordersRepository.save(orders);
        //when
        Orders queriedOrder = ordersRepository.findByCustomerId(orders.getCustomerId())
                .orElseThrow(() -> new EcommerceException("Order not found!"));

        //then
        Assertions.assertEquals(queriedOrder.getCustomerId(), orders.getCustomerId());
    }

    @Test
    void testFindByCustomerId_NotFound() {
        // given
        String exceptionMessage = "Order not found!";
        Orders orders = new Orders();
        orders.setId(1L);
        orders.setOrderDate(LocalDateTime.now());
        orders.setCustomerId(3L);
        orders.setAmount(BigDecimal.TEN);
        orders.setShippingAddress("shipping-address");
        orders.setStatus(Constants.ORDER_STATUS_PLACED);
        ordersRepository.save(orders);
        //then
        Orders queriedOrder = ordersRepository.findByCustomerId(15L).orElse(null);

        Assertions.assertEquals(queriedOrder, null);
    }

}
