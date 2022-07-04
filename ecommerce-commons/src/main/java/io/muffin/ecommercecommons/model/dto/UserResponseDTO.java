package io.muffin.ecommercecommons.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String password;
}
