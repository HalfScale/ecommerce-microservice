package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.exception.EcommerceException;
import io.muffin.inventoryservice.model.Product;
import io.muffin.inventoryservice.model.dto.ProductRequestDTO;
import io.muffin.inventoryservice.model.dto.ProductResponseDTO;
import io.muffin.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void testFindById_ProductFound() throws JsonProcessingException {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(getProduct()));
        when(objectMapper.writeValueAsString(any(Product.class))).thenReturn("some-string");
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(getProductResponseDTO());
        assertNotNull(productService.findById(1L));
    }

    @Test
    void testFindById_Throws_ProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EcommerceException.class, () -> productService.findById(1L),
                "Product not found!");
    }

    @Test
    void testCreateProduct() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any(ProductRequestDTO.class))).thenReturn("some-string");
        when(modelMapper.map(any(ProductRequestDTO.class), eq(Product.class))).thenReturn(getProduct());
        when(productRepository.save(any(Product.class))).thenReturn(getProduct());
        assertEquals(productService.createProduct(getProductRequestDTO()), 1L);
    }

    @Test
    void testDeleteProduct_ProductExisting() throws JsonProcessingException {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(getProduct()));
        doNothing().when(productRepository).delete(any(Product.class));
        assertEquals(productService.deleteProduct(1L), 1L);
    }

    @Test
    void testDeleteProduct_Throws_ProductNotFound() throws JsonProcessingException {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EcommerceException.class, () -> productService.deleteProduct(1L),
                "Product not found!");
    }

    private Product getProduct() {
        Product product = new Product();
        product.setId(1L);
        return product;
    }

    private ProductRequestDTO getProductRequestDTO() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setId(1L);
        return productRequestDTO;
    }

    private ProductResponseDTO getProductResponseDTO() {
        return new ProductResponseDTO();
    }


}
