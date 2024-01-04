package se.sprinta.headhunterbackend.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<User> users;

    @BeforeEach
    void setUp() {
        User u1 = new User();
        u1.setId("d66a3164-0a9d-4efb-943b-de64057aab14");
        u1.setUsername("Mikael");
        u1.setPassword("123456");
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setId("d66a3164-0a9d-4efb-943b-de64057aab15");
        u2.setUsername("Anders");
        u2.setPassword("654321");
        u2.setRoles("user");

        this.users = new ArrayList<>();
        this.users.add(u1);
        this.users.add(u2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllUsersSuccess() {
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
    void testSaveUsersSuccess() {
        // Setup
        User newUser = new User();
        newUser.setId("abc");
        newUser.setUsername("Mikael");
        newUser.setPassword("123456");
        newUser.setRoles("admin user");

        // Given
        given(this.userRepository.save(newUser)).willReturn(newUser);

        // When
        User returnedUser = this.userService.save(newUser);

        // Then
        assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());

        // Verify
        verify(this.userRepository, times(1)).save(newUser);
    }

}