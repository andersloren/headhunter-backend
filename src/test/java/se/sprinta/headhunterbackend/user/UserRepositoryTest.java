package se.sprinta.headhunterbackend.user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        User u1 = new User();
        u1.setEmail("m@e.se");
        u1.setUsername("Mikael");
        u1.setPassword("123456");
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setEmail("a@l.se");
        u2.setUsername("Anders");
        u2.setPassword("654321");
        u2.setRoles("user");

        this.entityManager.persist(u1);
        this.entityManager.persist(u2);
        this.entityManager.flush();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAll() {
        List<User> actualUsers = this.userRepository.findAll();

        assertEquals(2, actualUsers.size());
    }

    @Test
    void testFindByEmailSuccess() {
        User user = this.userRepository.findByEmail("m@e.se").get();

        assertEquals("m@e.se", user.getEmail());
        assertEquals("Mikael", user.getUsername());
        assertEquals("admin user", user.getRoles());
    }
}