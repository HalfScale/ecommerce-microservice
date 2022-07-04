package io.muffin.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.authservice.model.User;
import io.muffin.authservice.model.dto.LoginRequestDTO;
import io.muffin.authservice.model.dto.RegistrationRequestDTO;
import io.muffin.authservice.repository.UserRepository;
import io.muffin.authservice.util.JwtUtil;
import io.muffin.ecommercecommons.model.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public String registerUser(RegistrationRequestDTO registrationRequestDTO) {
        User user = modelMapper.map(registrationRequestDTO, User.class);
        user.setPassword(encoder.encode(registrationRequestDTO.getPassword()));
        userRepository.save(user);
        log.info("User registered successfully!");
        return "User successfully registered";
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDTO.getEmail(), loginRequestDTO.getPassword()
                        )
                );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        log.info("username/email => [{}]", user.getUsername());

        return jwtUtil.generateToken(user.getUsername());
    }

    public UserResponseDTO validateUser(String email) {
        UserResponseDTO dto = new UserResponseDTO();
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not existing"));
            dto = modelMapper.map(user, UserResponseDTO.class);
        } catch (UsernameNotFoundException ex) {
            log.info("Error occured while validating user: [{}]", ex.getMessage());
        }
        return dto;
    }
}
