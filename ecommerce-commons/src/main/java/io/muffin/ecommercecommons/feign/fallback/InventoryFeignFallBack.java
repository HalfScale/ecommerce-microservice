package io.muffin.ecommercecommons.feign.fallback;

import io.muffin.ecommercecommons.feign.InventoryFeignClient;
import io.muffin.ecommercecommons.feign.RestAuthConsumer;
import io.muffin.ecommercecommons.model.dto.ProductResponseDTO;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;

public class InventoryFeignFallBack implements InventoryFeignClient {

    @Override
    public ProductResponseDTO findProductById(long id) {
        return null;
    }
}
