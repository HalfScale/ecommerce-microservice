package io.muffin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrationRequestDTO {
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
