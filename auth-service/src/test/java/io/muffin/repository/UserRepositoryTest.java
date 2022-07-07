package io.muffin.repository;

import io.muffin.model.Roles;
import io.muffin.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Test
    void findUserByEmail() {
        // given
        String email = "admin@gmail.com";
        Roles roles = rolesRepository.findByName("ADMIN").orElse(null); // roles data is initialized through data.sql in resources folder

        User admin = new User(null, email, "password", "System",
                "Adminisitrator", roles);
        //when
        userRepository.save(admin);

        //then
        Assertions.assertEquals(userRepository.findByEmail(email).get(), admin);
    }

}
