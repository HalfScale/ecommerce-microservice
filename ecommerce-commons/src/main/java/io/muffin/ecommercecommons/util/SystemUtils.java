package io.muffin.ecommercecommons.util;

import io.muffin.ecommercecommons.exception.ValidationError;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class SystemUtils {

    private final JwtUtil JWT_UTIL = new JwtUtil();

    public boolean validateToken(String token) {

        if(JWT_UTIL.validateToken(token)) {
            return true;
        }
        throw new ValidationError("Invalid token!");
    }
}
