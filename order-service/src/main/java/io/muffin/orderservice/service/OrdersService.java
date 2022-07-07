package io.muffin.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.ValidationError;
import io.muffin.ecommercecommons.feign.RestAuthConsumer;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.orderservice.model.OrderItems;
import io.muffin.orderservice.model.Orders;
import io.muffin.orderservice.model.dto.OrderItemsRequestDTO;
import io.muffin.orderservice.model.dto.OrderRequestDTO;
import io.muffin.orderservice.repository.OrderItemsRepository;
import io.muffin.orderservice.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final RestAuthConsumer restAuthConsumer;

    public String placeOrder(OrderRequestDTO orderRequestDTO, String auth) throws JsonProcessingException {
        String token = auth.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);
        log.info("[{}] is placing an order", email);

        //call auth service to query the details of this particular user using feign client
        UserResponseDTO userResponseDTO = restAuthConsumer.validateEmail(email);

        if(userResponseDTO.getId() == null) {
            throw new ValidationError("Invalid email");
        }

        //if userResponseDTO for validation returns a null id then, throw an ValidationError

        Orders order = modelMapper.map(orderRequestDTO, Orders.class);
        order.setCustomerId(userResponseDTO.getId());
        order.setOrderDate(LocalDateTime.now());
        log.info("orders mapping result => [{}]", objectMapper.writeValueAsString(order));
        ordersRepository.save(order);

        for (OrderItemsRequestDTO orderItem : orderRequestDTO.getOrderItems()) {
            OrderItems item = modelMapper.map(orderItem, OrderItems.class);
            item.setOrderId(order);
            log.info("item mapping result => [{}]", objectMapper.writeValueAsString(item));
            orderItemsRepository.save(item);
        }

        return "Order Placed Successfully!";
    }
}
