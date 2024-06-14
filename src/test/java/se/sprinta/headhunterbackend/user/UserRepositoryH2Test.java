package se.sprinta.headhunterbackend.user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test-h2")
class UserRepositoryH2Test {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setEmail("admin-h2@hh.se");
        user1.setPassword("123");
        user1.setRoles("admin");

        User user2 = new User();
        user2.setEmail("user-h2@hh.se");
        user2.setPassword("abc");
        user2.setRoles("user");

        this.entityManager.persist(user1);
        this.entityManager.persist(user2);
        this.entityManager.flush();
        this.entityManager.clear();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindUserByEmailSuccess() {

        Optional<User> foundUser = this.userRepository.findUserByEmail("admin-h2@hh.se");
        if (foundUser.isPresent()) {
            assertEquals(foundUser.get().getEmail(), "admin-h2@hh.se");
            assertEquals(foundUser.get().getRoles(), "admin");
        }
    }

    @Test
    void testGetUserByEmailSuccess() {
        Optional<UserDtoView> foundUserDtoView = this.userRepository.getUserByEmail("admin-h2@hh.se");
        if (foundUserDtoView.isPresent()) {
            assertEquals(foundUserDtoView.get().email(), "admin-h2@hh.se");
            assertEquals(foundUserDtoView.get().roles(), "admin");
        }
    }
}