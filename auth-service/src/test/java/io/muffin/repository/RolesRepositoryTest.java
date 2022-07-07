package io.muffin.repository;

import io.muffin.model.Roles;
import io.muffin.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RolesRepositoryTest {

    @Autowired
    private RolesRepository rolesRepository;

    @Test
    void findRolesByName() {
        // given
        Roles myRole = new Roles(null, "ROLE");
        rolesRepository.save(myRole);
        //when
        Roles targetRole = rolesRepository.findByName(myRole.getName())
                .orElseThrow(() -> new RuntimeException("Role does not exist!"));

        //then
        Assertions.assertEquals(targetRole, myRole);
    }

}
