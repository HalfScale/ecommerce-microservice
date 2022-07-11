package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.ecommercecommons.model.dto.ErrorResponseDTO;
import io.muffin.inventoryservice.model.dto.ProductRequestDTO;
import io.muffin.inventoryservice.model.dto.ProductResponseDTO;
import io.muffin.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findById(@PathVariable String productId) throws JsonProcessingException {
        log.info("GET_PRODUCT_BY_ID => [{}]", productId);
        ProductResponseDTO productResponseDTO = productService.findById(Long.valueOf(productId));
        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping(value = "/product/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequestDTO productRequestDTO) throws JsonProcessingException {
        log.info("CREATE_PRODUCT => [{}]", objectMapper.writeValueAsString(productRequestDTO));
        return ResponseEntity.ok(productService.createProduct(productRequestDTO));
    }

    @PutMapping(path = "/product/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProduct(@RequestBody ProductRequestDTO productRequestDTO) throws JsonProcessingException {
        log.info("EDIT_PRODUCT => [{}]", "");
        return ResponseEntity.ok(productService.editProduct(productRequestDTO));
    }

    @DeleteMapping(path = "/product/delete/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteProduct(@PathVariable String productId) throws JsonProcessingException {
        log.info("DELETE_PRODUCT => [{}]", productId);
        return ResponseEntity.ok(productService.deleteProduct(Long.parseLong(productId)));
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
