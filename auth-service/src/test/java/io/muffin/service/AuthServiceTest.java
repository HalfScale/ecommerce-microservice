package io.muffin.service;

import io.muffin.ecommercecommons.jwt.JwtUserDetails;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.model.Roles;
import io.muffin.model.User;
import io.muffin.model.dto.LoginRequestDTO;
import io.muffin.model.dto.RegistrationRequestDTO;
import io.muffin.repository.RolesRepository;
import io.muffin.repository.UserRepository;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import io.muffin.service.model.AuthTestObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RolesRepository rolesRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegisterUser() {
        when(rolesRepository.findByName(any())).thenReturn(Optional.of(this.getRoles()));
        when(modelMapper.map(any(RegistrationRequestDTO.class), eq(User.class)))
                .thenReturn(this.getUser());
        when(encoder.encode(any())).thenReturn(new String());
        assertNotNull(authService.registerUser(this.getRegistrationRequestDTO()));
    }

    @Test
    void testLogin() {
        when(authenticationManager
                .authenticate(any(Authentication.class))).thenReturn(this.getAuthTestObject());
        when(jwtUtil.generateToken(any())).thenReturn(new String());
        assertNotNull(authService.login(this.getLoginRequestDTO()));
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    private UserResponseDTO getUserResponseDTO() {
        return new UserResponseDTO();
    }

    private Roles getRoles() {
        return new Roles();
    }

    private RegistrationRequestDTO getRegistrationRequestDTO() {
        return new RegistrationRequestDTO();
    }

    private UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken("", "");
    }

    private LoginRequestDTO getLoginRequestDTO() {
        return new LoginRequestDTO();
    }

    private JwtUserDetails getJwtUserDetails() {
        return new JwtUserDetails(1L,
                "email@gmail.com",
                "password",
                "name",
                "role");
    }

    private AuthTestObject getAuthTestObject() {
        return new AuthTestObject(this.getJwtUserDetails());
    }
}
