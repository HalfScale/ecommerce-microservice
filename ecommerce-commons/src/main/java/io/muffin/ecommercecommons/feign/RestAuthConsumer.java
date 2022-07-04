package io.muffin.ecommercecommons.feign;

import io.muffin.ecommercecommons.feign.fallback.RestAuthFallback;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", fallback = RestAuthFallback.class)
public interface RestAuthConsumer {

    @GetMapping("/auth/validate/user/{email}")
    public UserResponseDTO validateEmail(@PathVariable String email);
}
