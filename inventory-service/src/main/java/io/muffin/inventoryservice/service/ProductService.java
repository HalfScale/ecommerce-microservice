package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
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

    public ProductResponseDTO findById(long id) throws JsonProcessingException {
        Product product = productRepository
                .findById(id).orElseThrow(() -> new EcommerceException("Product not found!"));
        log.info("PRODUCT => [{}]", objectMapper.writeValueAsString(product));
        ProductResponseDTO response = modelMapper.map(product, ProductResponseDTO.class);
        return response;
    }

    public long createProduct(ProductRequestDTO productRequestDTO) throws JsonProcessingException {
        log.info("ADD_PRODUCT_REQUEST: {}", objectMapper.writeValueAsString(productRequestDTO));
        Product product = modelMapper.map(productRequestDTO, Product.class);
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    public long editProduct(ProductRequestDTO productRequestDTO) throws JsonProcessingException {
        log.info("EDIT_PRODUCT_REQUEST: {}", objectMapper.writeValueAsString(productRequestDTO));
        productRepository.findById(productRequestDTO.getId())
                .orElseThrow(() -> new EcommerceException("Product not found!"));
        Product product = modelMapper.map(productRequestDTO, Product.class);
        productRepository.save(product);
        return product.getId();
    }

    public long deleteProduct(long id) throws JsonProcessingException {
        log.info("DELETE_PRODUCT_ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EcommerceException("Product not found!"));
        productRepository.delete(product);
        return id;
    }


}
