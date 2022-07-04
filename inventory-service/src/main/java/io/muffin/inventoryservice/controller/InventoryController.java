package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.dto.ProductRequestDTO;
import io.muffin.inventoryservice.model.dto.ProductResponseDTO;
import io.muffin.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> findByProductId(@PathVariable String productId) throws JsonProcessingException {
        log.info("findByProductId request => [{}]", productId);
        ProductResponseDTO productResponseDTO = productService.findById(Long.valueOf(productId));
        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequestDTO productRequestDTO) throws JsonProcessingException {
        log.info("createProduct request => [{}]", objectMapper.writeValueAsString(productRequestDTO));
        return ResponseEntity.ok(productService.createProduct(productRequestDTO));
    }
}
