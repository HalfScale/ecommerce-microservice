package io.muffin.orderservice.exception;

import feign.FeignException;
import io.muffin.ecommercecommons.model.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FeignException.class)
    public ErrorResponseDTO handleFeignStatusException(FeignException ex, HttpServletResponse response) {
        String exceptionMessage = "Resource not found";
        ErrorResponseDTO<Map<String, String>> errorResponse = new ErrorResponseDTO();
        errorResponse.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.setMessage(exceptionMessage);
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

}
