package io.muffin.authservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.authservice.model.dto.LoginRequestDTO;
import io.muffin.authservice.model.dto.RegistrationRequestDTO;
import io.muffin.authservice.service.UserService;
import io.muffin.authservice.util.JwtUtil;
import io.muffin.ecommercecommons.model.dto.ErrorResponseDTO;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO) throws JsonProcessingException {
        log.info("register user => [{}]", objectMapper.writeValueAsString(registrationRequestDTO));
        return userService.registerUser(registrationRequestDTO);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws JsonProcessingException {
        log.info("login => [{}]", objectMapper.writeValueAsString(loginRequestDTO));
        return userService.login(loginRequestDTO);
    }

    @GetMapping("/validate/user/{email}")
    public UserResponseDTO validateUser(@PathVariable String email) throws InterruptedException {
        log.info("validating user email => [{}]", email);
        return userService.validateUser(email);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDTO handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponseDTO<Map<String, String>> errorResponse = new ErrorResponseDTO();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setData(errors);
        errorResponse.setMessage("404 Bad Request");
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }
}
