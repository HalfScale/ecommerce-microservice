package io.muffin.config;

import io.muffin.model.Roles;
import io.muffin.model.User;
import io.muffin.repository.RolesRepository;
import io.muffin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

//@Configuration
public class InitConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void createAdminUser() {
        User admin  = userRepository.findByEmail(adminEmail).orElse(null);
        if(admin == null) {
            Roles role = rolesRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE NOT EXISTING!"));
            User newUserAdmin = new User(-1L, "admin@gmail.com", encoder.encode(adminPassword),
                    "System", "Administrator", role);
            userRepository.save(newUserAdmin);
        }
    }
}
