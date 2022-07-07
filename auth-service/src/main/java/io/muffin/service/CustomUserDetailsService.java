package io.muffin.service;

import io.muffin.ecommercecommons.jwt.JwtUserDetails;
import io.muffin.model.User;
import io.muffin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("EMAIL_NOT_FOUND => [{}]", email)));
        return new JwtUserDetails(user.getId(), user.getEmail(), user.getPassword(),
                String.format("%s %s", user.getFirstName(), user.getLastName()), user.getRoles().getName());
    }
}
