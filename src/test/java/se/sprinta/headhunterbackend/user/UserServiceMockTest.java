package se.sprinta.headhunterbackend.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<User> users = new ArrayList<>();

    @Test
    void testFindAllUsersSuccess() {
        // Given
        given(this.userRepository.findAll()).willReturn(this.users);

        // When
        List<User> actualUsers = this.userService.findAll();
        System.out.println(actualUsers.size());

        // Then
        assertThat(actualUsers.size()).isEqualTo(this.users.size());

        // Verify
        then(this.userRepository).should().findAll();
    }


    @Test
    void testFindUserByEmailSuccess() {
        User user = new User();
        user.setEmail("admin@hh.se");
        user.setRoles("admin");

        given(this.userRepository.findUserByEmail("admin@hh.se")).willReturn(Optional.of(user));

        // When
        User returnedUser = this.userService.findUserByEmail("admin@hh.se");

        // Then
        assertEquals(returnedUser.getEmail(), "admin@hh.se");
        assertEquals(returnedUser.getRoles(), "admin");

        // Verify
        then(this.userRepository).should().findUserByEmail("admin@hh.se");
    }

    @Test
    void testFindUserByEmailWithNonExistentEmail() {
        // Given
        given(this.userRepository.findUserByEmail("abc")).willThrow(new ObjectNotFoundException("user", "abc"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.findUserByEmail("abc");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email abc");

        // Verify
        then(this.userRepository).should().findUserByEmail("abc");
    }

    @Test
    void testGetUserByEmailSuccess() {
        UserDtoView userDtoView = new UserDtoView(
                "admin@hh.se",
                "admin",
                0
        );

        given(this.userRepository.getUserByEmail("admin@hh.se")).willReturn(Optional.of(userDtoView));

        // When
        UserDtoView returnedUserDtoView = this.userService.getUserByEmail("admin@hh.se");

        // Then
        assertEquals(returnedUserDtoView.email(), "admin@hh.se");
        assertEquals(returnedUserDtoView.roles(), "admin");
        assertEquals(returnedUserDtoView.number_of_jobs(), 0);

        // Verify
        then(this.userRepository).should().getUserByEmail("admin@hh.se");
    }

    @Test
    void testGetUserByEmailWithNonExistentEmail() {
        // Given
        given(this.userRepository.getUserByEmail("abc")).willThrow(new ObjectNotFoundException("user", "abc"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.getUserByEmail("abc");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email abc");

        // Verify
        then(this.userRepository).should().getUserByEmail("abc");
    }

    @Test
    void testSaveUserSuccess() {
        // Setup
        User newUser = new User();
        newUser.setEmail("admin@hh.se");
        newUser.setPassword("123456");
        newUser.setRoles("admin");

        // Given
        given(this.userRepository.save(newUser)).willReturn(newUser);
        given(this.passwordEncoder.encode(newUser.getPassword())).willReturn("Encoded Password");

        // When
        User returnedUser = this.userService.save(newUser);

        // Then
        assertEquals(returnedUser.getEmail(), "admin@hh.se");
        assertEquals(returnedUser.getRoles(), "admin");

        // Verify
        then(this.userRepository).should().save(newUser);
    }

    @Test
    void testUpdateOwnUserSuccess() {
        User ownUser = new User();
        ownUser.setEmail("m@e.se");
        ownUser.setPassword("123456");
        ownUser.setRoles("admin user");

        User update = new User();
        update.setRoles("admin"); // From admin user to just admin

        String email = "m@e.se";
        String roles = "admin";

        // Given
        given(this.userRepository.findUserByEmail(email)).willReturn(Optional.of(ownUser));
        given(this.userRepository.save(ownUser)).willReturn(ownUser);

        // When
        User updatedUser = this.userService.update(email, update);

        // Then
        assertThat(updatedUser.getEmail()).isEqualTo(ownUser.getEmail());
        assertThat(updatedUser.getRoles()).isEqualTo(roles);

        // Verify
        then(this.userRepository).should().findUserByEmail(email);
        then(this.userRepository).should().save(ownUser);
    }

    @Test
    void testUpdateUserWithNonExistentId() {
        String email = "abc";
        User update = new User();
        update.setRoles("admin"); // From admin user to just admin

        // Given
        given(this.userRepository.findUserByEmail(email)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            User updatedUser = this.userService.update("abc", update);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email abc");

        // Verify
        then(this.userRepository).should().findUserByEmail("abc");
    }

    @Test
    void testDeleteUserSuccess() {
        User user = new User();
        user.setEmail("user@hh.se");
        user.setRoles("user");

        // Given
        given(this.userRepository.findUserByEmail("user@hh.se")).willReturn(Optional.of(user));
        willDoNothing().given(this.userRepository).delete(user);

        // When
        this.userService.delete("user@hh.se");

        // Then
        then(this.userRepository).should().delete(user);
    }

    @Test
    void testDeleteUserWithNonExistentId() {
        // Given
        given(this.userRepository.findUserByEmail("abc")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete("abc");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email abc");

        // Verify
        then(this.userRepository).should().findUserByEmail("abc");
    }

}