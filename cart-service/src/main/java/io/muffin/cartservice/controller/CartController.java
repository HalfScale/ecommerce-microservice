package io.muffin.cartservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.muffin.cartservice.model.dto.CartItemDTO;
import io.muffin.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestHeader("authorization") String auth,
                            @RequestBody CartItemDTO cartItemDTO) throws JsonProcessingException {
        log.info("intercepted auth => [{}]", auth);
        log.info("intercepted cartItemDTO => [{}]", cartItemDTO);

        return cartService.addToCart(auth, cartItemDTO);
    }
}
