package io.muffin.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank(message = "email is required")
    @Email
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
