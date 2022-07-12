package io.muffin.orderservice.repository;

import io.muffin.orderservice.model.OrderItems;
import io.muffin.orderservice.model.Orders;
import io.muffin.orderservice.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class OrderItemsRepositoryTest {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    void testFindAllByOrderId() {
        // given
        Orders orders = new Orders();
        orders.setId(null);
        orders.setOrderDate(LocalDateTime.now());
        orders.setCustomerId(4L);
        orders.setAmount(BigDecimal.TEN);
        orders.setShippingAddress("shipping-address");
        orders.setStatus(Constants.ORDER_STATUS_PLACED);
        ordersRepository.save(orders);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orders);
        orderItem.setPrice(BigDecimal.valueOf(10000));
        orderItem.setProductId(1L);
        orderItem.setQuantity(10);
        orderItemsRepository.save(orderItem);

        //when
        Optional<List<OrderItems>> orderItems = orderItemsRepository.findAllByOrderId(orders);

        //then
        Assertions.assertNotEquals(orderItems.get().size(), 0);
    }

    @Test
    void testFindAllByOrderId_NotFound() {
        // given
        Orders ordersOne = new Orders();
        ordersOne.setId(null);
        ordersOne.setOrderDate(LocalDateTime.now());
        ordersOne.setCustomerId(4L);
        ordersOne.setAmount(BigDecimal.TEN);
        ordersOne.setShippingAddress("shipping-address");
        ordersOne.setStatus(Constants.ORDER_STATUS_PLACED);
        ordersRepository.save(ordersOne);

        Orders ordersTwo = new Orders();
        ordersTwo.setId(null);
        ordersTwo.setOrderDate(LocalDateTime.now());
        ordersTwo.setCustomerId(4L);
        ordersTwo.setAmount(BigDecimal.TEN);
        ordersTwo.setShippingAddress("shipping-address");
        ordersTwo.setStatus(Constants.ORDER_STATUS_PLACED);
        ordersRepository.save(ordersTwo);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(ordersOne);
        orderItem.setPrice(BigDecimal.valueOf(10000));
        orderItem.setProductId(1L);
        orderItem.setQuantity(10);
        orderItemsRepository.save(orderItem);

        //when
        Optional<List<OrderItems>> orderItems = orderItemsRepository.findAllByOrderId(ordersTwo);

        //then
        Assertions.assertEquals(orderItems.get().size(), 0);
    }

}
