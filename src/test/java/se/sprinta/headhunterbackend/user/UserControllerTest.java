package se.sprinta.headhunterbackend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<User> users;

    @Value("${api.endpoint.base-url-users}")
    String baseUrlUsers;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
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

        this.users = new ArrayList<>();
        this.users.add(u1);
        this.users.add(u2);

        // Given
        given(this.userService.findAll()).willReturn(this.users);

        // When and then
        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data[0].username").value("Mikael"))
                .andExpect(jsonPath("$.data[0].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin user"))
                .andExpect(jsonPath("$.data[1].username").value("Anders"))
                .andExpect(jsonPath("$.data[1].email").value("a@l.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
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

        this.users = new ArrayList<>();
        this.users.add(u1);
        this.users.add(u2);

        // Given
        given(this.userService.findByUserEmail("a@l.se")).willReturn(this.users.get(1));

        // When and then
        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/" + "a@l.se").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One User Success"))
                .andExpect(jsonPath("$.data.email").value("a@l.se"))
                .andExpect(jsonPath("$.data.username").value("Anders"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testFindUserByIdWithNonExistentId() throws Exception {
        String nonExistingEmail = "abc";

        // Given
        given(this.userService.findByUserEmail("abc")).willThrow(new ObjectNotFoundException("user", "abc"));

        // When and then
        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/" + nonExistingEmail).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        // Setup
        User newUser = new User();
        newUser.setEmail("m@j.se");
        newUser.setUsername("Mehrdad Javan");
        newUser.setPassword("2468");
        newUser.setRoles("admin");

        String json = this.objectMapper.writeValueAsString(newUser);

        // Given
        given(this.userService.save(Mockito.any(User.class))).willReturn(newUser);

        // When and then
        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add User Success"))
                .andExpect(jsonPath("$.data.username").value("Mehrdad Javan"))
                .andExpect(jsonPath("$.data.roles").value("admin"));
    }

    // TODO: 31/01/2024 testAddUserSuccess
    // TODO: 31/01/2024 testAddUserInvalidInput

    @Test
    void testUpdateUserSuccess() throws Exception {

        UserDtoView userDto = new UserDtoView(
                "m@e.se",
                "Mikael",
                "admin user",
                0);

        User updatedUser = new User();
        updatedUser.setEmail("m@e.se");
        updatedUser.setUsername("Mikael");
        updatedUser.setPassword("123456");
        updatedUser.setRoles("admin"); // Role changes from 'admin user' to 'admin'

        String email = "m@e.se";

        String json = this.objectMapper.writeValueAsString(userDto);

        // Given
        given(this.userService.update(eq(email), Mockito.any(String.class))).willReturn(updatedUser);

        // When and then
        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success"))
                .andExpect(jsonPath("$.data.email").value("m@e.se"))
                .andExpect(jsonPath("$.data.username").value("Mikael"))
                .andExpect(jsonPath("$.data.roles").value("admin"))
                .andExpect(jsonPath("$.data.numberOfJobs").value(0));
    }

    @Test
    void testUpdateUserWithNonExistentEmail() throws Exception {
        String email = "abc";
        String roles = "admin";

        // Given
        given(this.userService.update(email, roles)).willThrow(new ObjectNotFoundException("user", email));

        // When and then
        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/" + email)
                        .content(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(roles)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserByIdSuccess() throws Exception {
        String existingEmail = "a@l.se";

        // Given
        doNothing().when(this.userService).delete("a@l.se");

        // When and then
        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/" + existingEmail).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete User Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserByIdWithNonExistingId() throws Exception {
        String nonExistingEmail = "abc";
        // Given
        doThrow(new ObjectNotFoundException("user", nonExistingEmail)).when(this.userService).delete(nonExistingEmail);

        // When and then
        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/" + nonExistingEmail)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}