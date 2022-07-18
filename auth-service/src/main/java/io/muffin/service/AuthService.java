package io.muffin.service;

import io.muffin.ecommercecommons.jwt.JwtUserDetails;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import io.muffin.model.Roles;
import io.muffin.model.User;
import io.muffin.model.dto.LoginRequestDTO;
import io.muffin.model.dto.RegistrationRequestDTO;
import io.muffin.repository.RolesRepository;
import io.muffin.repository.UserRepository;
import io.muffin.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RolesRepository rolesRepository;

    public long registerUser(RegistrationRequestDTO registrationRequestDTO) {
        Roles role = rolesRepository.findByName(Constants.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Resource not found!"));

        User user = modelMapper.map(registrationRequestDTO, User.class);
        user.setPassword(encoder.encode(registrationRequestDTO.getPassword()));
        user.setRoles(role);
        userRepository.save(user);
        log.info("REGISTRATION_SUCCESSFUL => [{}]", user.getEmail());
        return user.getId();
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        // CustomUserDetailsService will be called here in authenticating
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        JwtUserDetails user = (JwtUserDetails) authentication.getPrincipal();
        log.info("LOGIN_USER => [{}]", user.getUsername());
        return jwtUtil.generateToken(user);
    }
}
