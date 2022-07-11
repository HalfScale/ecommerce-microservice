package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.Product;
import io.muffin.inventoryservice.model.dto.ProductRequestDTO;
import io.muffin.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.yml")
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setup() {
//        productRepository.de();
    }

    @BeforeEach
    void insertProduct() {
//        createNewProduct(null);
    }

    @Test
    void testGetProduct() throws Exception {
        Product product = createNewProduct(1L);
        mvc.perform(get("/inventory/product/1"))
                .andExpect(content().json(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductRequestDTO productRequestDTO = createProductRequestDTO(null);
        mvc.perform(post("/inventory/product/add").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("1"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditProduct() throws Exception {
        createNewProduct(null);
        createNewProduct(null);
        ProductRequestDTO productRequestDTO = createProductRequestDTO(2L);
        mvc.perform(put("/inventory/product/edit")
                .content(objectMapper.writeValueAsString(productRequestDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("2"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteProduct() throws Exception {
        createNewProduct(null);
        mvc.perform(delete("/inventory/product/delete/1"))
                .andExpect(content().json("1"))
                .andExpect(status().isOk());

        Assertions.assertEquals(productRepository.findById(1L), Optional.empty());
    }

    private Product createNewProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setDescription("Iphone test");
        product.setName("Iphone Test");
        product.setPrice(BigDecimal.valueOf(10000));
        product.setStock(10);
        productRepository.save(product);
        return product;
    }

    private ProductRequestDTO createProductRequestDTO(Long id) {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setId(id);
        productRequestDTO.setDescription("product-description");
        productRequestDTO.setName("product-name");
        productRequestDTO.setPrice(new BigDecimal(30000));
        productRequestDTO.setStock(20);
        return productRequestDTO;
    }
}
