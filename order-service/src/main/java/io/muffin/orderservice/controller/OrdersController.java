package io.muffin.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.muffin.ecommercecommons.exception.ValidationError;
import io.muffin.ecommercecommons.model.dto.ErrorResponseDTO;
import io.muffin.orderservice.model.dto.OrderRequestDTO;
import io.muffin.orderservice.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrdersController {

    private final OrdersService ordersService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public String placeOrder(@RequestBody OrderRequestDTO orderRequestDTO,
                             @RequestHeader("authorization") String auth) throws JsonProcessingException {
        log.info("placeOrder request => [{}]", objectMapper.writeValueAsString(orderRequestDTO));
        log.info("Authorization header => [{}]", auth != null ? auth : "No authorization header");
        return ordersService.placeOrder(orderRequestDTO, auth);
    }


    public String fallbackResponse(OrderRequestDTO orderRequestDTO, String auth) {
        return "The service requested maybe slow or down.";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationError.class)
    public ErrorResponseDTO handleValidationExceptions(
            ValidationError ex) {

        ErrorResponseDTO<Map<String, String>> errorResponse = new ErrorResponseDTO();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setMessage("Invalid email");
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }
}
