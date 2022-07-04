package io.muffin.ecommercecommons.feign.fallback;

import io.muffin.ecommercecommons.feign.RestAuthConsumer;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;

public class RestAuthFallback implements RestAuthConsumer {
    @Override
    public UserResponseDTO validateEmail(String email) {
        return new UserResponseDTO();
    }
}
