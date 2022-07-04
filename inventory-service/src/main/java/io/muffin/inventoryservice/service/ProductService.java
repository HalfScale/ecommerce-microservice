package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.Product;
import io.muffin.inventoryservice.model.dto.ProductRequestDTO;
import io.muffin.inventoryservice.model.dto.ProductResponseDTO;
import io.muffin.inventoryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public ProductResponseDTO findById(Long id) throws JsonProcessingException {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("No existing product"));
        log.info("retrieved product => [{}]", objectMapper.writeValueAsString(product));
        ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);
        return dto;
    }

    public String createProduct(ProductRequestDTO productRequestDTO) {
        productRepository.save(modelMapper.map(productRequestDTO, Product.class));
        log.info("Product successfully created!");
        return "Product Successfully created!";
    }
}
