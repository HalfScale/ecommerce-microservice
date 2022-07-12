package io.muffin.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.model.dto.ErrorResponseDTO;
import io.muffin.orderservice.model.dto.OrderRequestDTO;
import io.muffin.orderservice.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOrder(@RequestHeader("authorization") String auth) {
        log.info("GET_ORDER_AUTH => [{}]", auth);
        return ResponseEntity.ok(ordersService.getOrder(auth));
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO,
                                             @RequestHeader("authorization") String auth) throws JsonProcessingException {
        log.info("PLACE_ORDER => [{}]", objectMapper.writeValueAsString(orderRequestDTO));
        log.info("AUTH_HEADER => [{}]", auth != null ? auth : "No authorization header");
        return ResponseEntity.ok(ordersService.checkoutOrder(orderRequestDTO, auth));
    }

    @PutMapping(path = "/cancel/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> cancelOrder(@PathVariable Long orderId) {
        log.info("CANCEL_ORDER => [{}]", orderId);
        return ResponseEntity.ok(ordersService.cancelOrder(orderId));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {EcommerceException.class})
    public ErrorResponseDTO handleEcommerceExceptions(
            EcommerceException ex) {
        ErrorResponseDTO<Map<String, String>> errorResponse = new ErrorResponseDTO();
        errorResponse.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.setMessage(ex.getLocalizedMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

}
