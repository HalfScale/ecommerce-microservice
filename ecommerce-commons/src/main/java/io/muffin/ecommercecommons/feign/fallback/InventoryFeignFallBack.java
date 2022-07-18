package io.muffin.ecommercecommons.feign.fallback;

import io.muffin.ecommercecommons.feign.InventoryFeignClient;
import io.muffin.ecommercecommons.model.dto.ProductRequestDTO;
import io.muffin.ecommercecommons.model.dto.ProductResponseDTO;

public class InventoryFeignFallBack implements InventoryFeignClient {

    @Override
    public ProductResponseDTO findProductById(long id) {
        return null;
    }

    @Override
    public long updateProduct(ProductRequestDTO productRequestDTO) {
        return 0;
    }
}
