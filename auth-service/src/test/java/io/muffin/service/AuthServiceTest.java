package io.muffin.service;

import io.muffin.model.User;
import io.muffin.repository.UserRepository;
import io.muffin.util.JwtUtil;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @InjectMocks
    private AuthService authService;

    @Test
    public void test() {
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(this.getUser()));
        when(modelMapper.map(Mockito.any(User.class), Mockito.eq(UserResponseDTO.class))).thenReturn(this.getUserResponseDTO());
        assertNotNull(authService.validateUser("email@gmail.com"));
    }

    private User getUser() {
        return new User();
    }

    private UserResponseDTO getUserResponseDTO() {
        return new UserResponseDTO();
    }
}
