package se.sprinta.headhunterbackend.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test-h2")
public class UserServiceH2Test {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private List<User> users = new ArrayList<>();

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

        this.userRepository.save(user1);
        this.userRepository.save(user2);

        this.users.add(user1);
        this.users.add(user2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllUsers() {
        List<User> actualUsers = this.userService.findAll();
        assertEquals(actualUsers.size(), this.users.size());
    }

    @Test
    void testFindUserByEmailSuccess() {
        User foundUser = this.userService.findUserByEmail("admin-h2@hh.se");

        assertEquals(foundUser.getEmail(), "admin-h2@hh.se");
        assertEquals(foundUser.getRoles(), "admin");
    }

    @Test
    void testFindUserByEmailInvalidEmail() {
        String invalidEmail = "invalid email";
        assertThrows(ObjectNotFoundException.class,
                () -> this.userService.findUserByEmail(invalidEmail));
    }

    @Test
    void testGetUserByEmailSuccess() {
        UserDtoView foundUserDtoView = this.userService.getUserByEmail("admin-h2@hh.se");

        assertEquals(foundUserDtoView.email(), "admin-h2@hh.se");
        assertEquals(foundUserDtoView.roles(), "admin");
    }

    @Test
    void testGetUserByEmailInvalidEmail() {
        String invalidEmail = "invalid email";
        assertThrows(ObjectNotFoundException.class,
                () -> this.userService.findUserByEmail(invalidEmail));
    }

    @Test
    void testSaveUserSuccess() {
        User newUser = new User();
        newUser.setEmail("newUser@hh.se");
        newUser.setPassword("password");
        newUser.setRoles("user");

        User savedUser = this.userService.save(newUser);
        assertEquals(savedUser.getEmail(), newUser.getEmail());
        assertEquals(savedUser.getRoles(), newUser.getRoles());
    }

    @Test
    void testUpdateUserSuccess() {
        User update = new User();
        update.setEmail("newEmail-h2@hh.se");
        update.setRoles("admin");

        User updatedUser = this.userService.update("user-h2@hh.se", update);
        assertEquals(updatedUser.getRoles(), update.getRoles());
    }

    @Test
    void testUpdateUserInvalidEmail() {
        String invalidEmail = "invalid email";
        assertThrows(ObjectNotFoundException.class,
                () -> this.userService.findUserByEmail(invalidEmail));
    }

    @Test
    void testDeleteUserSuccess() {
        this.userService.delete("user-h2@hh.se");
        List<User> allUsers = this.userService.findAll();
        assertEquals(allUsers.size(), 2);
        assertThrows(ObjectNotFoundException.class, () -> this.userService.findUserByEmail("user-h2@hh.se"));
    }

    @Test
    void testDeleteUserInvalidEmail() {
        String invalidEmail = "invalid email";
        assertThrows(ObjectNotFoundException.class,
                () -> this.userService.findUserByEmail(invalidEmail));
    }
}
