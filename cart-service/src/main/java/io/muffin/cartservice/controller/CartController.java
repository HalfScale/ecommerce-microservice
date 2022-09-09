package io.muffin.cartservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.service.CartService;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.ecommercecommons.model.dto.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserCart(@RequestHeader("authorization") String auth,
                                              Pageable pageable) {
        log.info("Session ID: [{}]", RequestContextHolder.currentRequestAttributes().getSessionId());
        String token = jwtUtil.extractTokenFromHeader(auth);
        log.info("GET_CART => [{}]", token);
        return ResponseEntity.ok(cartService.getUserCart(token, pageable));
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addToCart(@RequestHeader("authorization") String auth,
                            @RequestBody CartItemDTO cartItemDTO) throws JsonProcessingException {
        String token = jwtUtil.extractTokenFromHeader(auth);
        log.info("INTERCEPTED_TOKEN => [{}]", token);
        log.info("ADD_CART_ITEM => [{}]", objectMapper.writeValueAsString(cartItemDTO));
        return ResponseEntity.ok(cartService.addToCart(token, cartItemDTO));
    }

    @PutMapping(path = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editCart(@RequestHeader("authorization") String auth,
                                           @RequestBody CartItemDTO cartItemDTO) throws JsonProcessingException {
        String token = jwtUtil.extractTokenFromHeader(auth);
        log.info("EDIT_CART_ITEM => [{}]", objectMapper.writeValueAsString(cartItemDTO));
        return ResponseEntity.ok(cartService.editCart(token, cartItemDTO));
    }

    @DeleteMapping(path = "/delete/{cartItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteCart(@RequestHeader("authorization") String auth,
                                             @PathVariable long cartItemId) {
        log.info("DELETE_CART_ITEM => [{}]", cartItemId);
        String token = jwtUtil.extractTokenFromHeader(auth);
        return ResponseEntity.ok(cartService.deleteCart(token, cartItemId));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EcommerceException.class)
    public ErrorResponseDTO handleNotFoundResources(
            EcommerceException ex) {

        ErrorResponseDTO<String> errorResponse = new ErrorResponseDTO();
        errorResponse.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.setData(ex.getLocalizedMessage());
        errorResponse.setMessage("404 Not found");
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }
}
