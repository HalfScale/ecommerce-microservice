package io.muffin.ecommercecommons.feign;

import io.muffin.ecommercecommons.feign.fallback.RestAuthFallback;
import io.muffin.ecommercecommons.model.dto.ProductResponseDTO;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", fallback = RestAuthFallback.class)
public interface InventoryFeignClient {

    @GetMapping("/inventory/product/{id}")
    public ProductResponseDTO findProductById(@PathVariable long id);
}
