package io.muffin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.model.dto.LoginRequestDTO;
import io.muffin.model.dto.RegistrationRequestDTO;
import io.muffin.model.dto.TokenResponseDTO;
import io.muffin.service.AuthService;
import io.muffin.ecommercecommons.model.dto.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO) throws JsonProcessingException {
        log.info("REGISTER_USER => [{}]", objectMapper.writeValueAsString(registrationRequestDTO));
        return ResponseEntity.ok(authService.registerUser(registrationRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws JsonProcessingException {
        log.info("LOGIN => [{}]", objectMapper.writeValueAsString(loginRequestDTO));

        String jwtToken = authService.login(loginRequestDTO);
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        tokenResponseDTO.setToken(jwtToken);
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @GetMapping("/validate/token/{token}")
    public ResponseEntity<Object> validateToken(@PathVariable String token) throws InterruptedException {
        log.info("VALIDATE_TOKEN => [{}]", token);
        return ResponseEntity.ok(authService.validateToken(token));
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
        errorResponse.setMessage("400 Bad Request");
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }
}
