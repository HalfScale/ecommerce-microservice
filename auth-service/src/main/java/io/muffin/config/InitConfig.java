package io.muffin.config;

import io.muffin.model.Roles;
import io.muffin.model.User;
import io.muffin.repository.RolesRepository;
import io.muffin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;

// using the this config, for initializing of admin account
@Configuration
@Profile(value = {"test", "prod"})
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
        initRoleData(); // init roles
        // create admin at the initial state of db
        User admin  = userRepository.findByEmail(adminEmail).orElse(null);
        if(admin == null) {
            Roles role = rolesRepository.findByName("ADMIN").orElse(null);
            User newUserAdmin = new User(-1L, adminEmail, encoder.encode(adminPassword),
                    "System", "Administrator", role);
            userRepository.save(newUserAdmin);
        }
    }

    private void initRoleData() {
        Roles admin = new Roles(null, "ADMIN");
        Roles user = new Roles(null, "USER");
        rolesRepository.saveAll(Arrays.asList(admin, user));
    }
}
