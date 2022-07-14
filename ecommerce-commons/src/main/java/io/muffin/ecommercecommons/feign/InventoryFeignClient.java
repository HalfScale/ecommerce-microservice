package io.muffin.ecommercecommons.feign;

import io.muffin.ecommercecommons.feign.fallback.InventoryFeignFallBack;
import io.muffin.ecommercecommons.model.dto.ProductRequestDTO;
import io.muffin.ecommercecommons.model.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "inventory-service", fallback = InventoryFeignFallBack.class)
public interface InventoryFeignClient {

    @GetMapping("/inventory/product/{id}")
    ProductResponseDTO findProductById(@PathVariable long id);

    @PutMapping("/inventory/product/edit")
    long updateProduct(ProductRequestDTO productRequestDTO);
}
