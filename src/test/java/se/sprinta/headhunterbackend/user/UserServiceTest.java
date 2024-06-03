package se.sprinta.headhunterbackend.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<User> users;

    @BeforeEach
    void setUp() {
    }


    @Test
    void testFindAllUsersSuccess() {
        User u1 = new User();
        u1.setEmail("m@e.se");
        u1.setPassword("a");
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setEmail("a@l.se");
        u2.setPassword("a");
        u2.setRoles("user");

        this.users = new ArrayList<>();
        this.users.add(u1);
        this.users.add(u2);

        // Given
        given(this.userRepository.findAll()).willReturn(this.users);

        // When
        List<User> actualUsers = this.userService.findAll();

        // Then
        assertThat(actualUsers.size()).isEqualTo(this.users.size());

        // Verify
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void testFindUserByEmailSuccess() {
        // Given
        User u1 = new User();
        u1.setEmail("m@e.se");
        u1.setPassword("123456");
        u1.setRoles("admin user");

        given(this.userRepository.findByEmail("m@e.se")).willReturn(Optional.of(u1));

        // When
        User returnedUser = this.userService.findByUserEmail("m@e.se");

        // Then
        assertThat(returnedUser.getEmail()).isEqualTo(u1.getEmail());
        assertThat(returnedUser.getRoles()).isEqualTo(u1.getRoles());
    }

    @Test
    void testFindUserByEmailWithNonExistentEmail() {
        // Given
        given(this.userRepository.findByEmail("abc")).willThrow(new ObjectNotFoundException("user", "abc"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            User user = this.userService.findByUserEmail("abc");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email abc");
    }

    @Test
    void testSaveUserSuccess() {
        // Setup
        User newUser = new User();
        newUser.setEmail("m@e.se");
        newUser.setPassword("123456");
        newUser.setRoles("admin user");

        // Given
        given(this.userRepository.save(newUser)).willReturn(newUser);
        given(this.passwordEncoder.encode(newUser.getPassword())).willReturn("Encoded Password");

        // When
        User returnedUser = this.userService.save(newUser);

        // Then
        assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());

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
        given(this.userRepository.findByEmail(email)).willReturn(Optional.of(ownUser));
        given(this.userRepository.save(ownUser)).willReturn(ownUser);

        // When
        User updatedUser = this.userService.update(email, update);

        // Then
        assertThat(updatedUser.getEmail()).isEqualTo(ownUser.getEmail());
        assertThat(updatedUser.getRoles()).isEqualTo(roles);

        // Verify
        verify(this.userRepository, times(1)).findByEmail(email);
        verify(this.userRepository, times(1)).save(ownUser);
    }

    @Test
    void testUpdateUserWithNonExistentId() {
        String email = "abc";
        User update = new User();
        update.setRoles("admin"); // From admin user to just admin

        // Given
        given(this.userRepository.findByEmail(email)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            User updatedUser = this.userService.update("abc", update);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email abc");
        verify(this.userRepository, times(1)).findByEmail("abc");
    }

    @Test
    void testDeleteUserSuccess() {
        User u1 = new User();
        u1.setEmail("m@e.se");
        u1.setPassword("123456");
        u1.setRoles("admin user");

        // Given
        given(this.userRepository.findByEmail("m@e.se")).willReturn(Optional.of(u1));
        doNothing().when(this.userRepository).delete(u1);

        // When
        this.userService.delete("m@e.se");

        // Then
        verify(this.userRepository, times(1)).findByEmail("m@e.se");
    }

    @Test
    void testDeleteUserWithNonExistentId() {
        User u1 = new User();
        u1.setEmail("m@e.se");

        // Given
        given(this.userRepository.findByEmail("abc")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete("abc");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email abc");
    }

}