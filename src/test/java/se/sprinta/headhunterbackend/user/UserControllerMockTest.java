package se.sprinta.headhunterbackend.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.dto.UserDtoForm;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class UserControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<User> users = new ArrayList<>();

    @Value("${api.endpoint.base-url-users}")
    String baseUrlUsers;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setEmail("admin@hh.se");
        user1.setPassword("123");
        user1.setRoles("admin");

        User user2 = new User();
        user2.setEmail("user@hh.se");
        user2.setPassword("abc");
        user2.setRoles("user");

        this.users.add(user1);
        this.users.add(user2);
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        // Given
        given(this.userService.findAll()).willReturn(this.users);

        // When and then
        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[1].email").value("user@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        // Given
        given(this.userService.findUserByEmail("user@hh.se")).willReturn(this.users.get(1));

        // When and then
        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/" + "user@hh.se").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One User Success"))
                .andExpect(jsonPath("$.data.email").value("user@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testFindUserByIdWithNonExistentId() throws Exception {
        String nonExistingEmail = "abc";

        // Given
        given(this.userService.findUserByEmail("abc")).willThrow(new ObjectNotFoundException("user", "abc"));

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
        newUser.setEmail("newUser@hh.se");
        newUser.setPassword("123");

        String json = this.objectMapper.writeValueAsString(newUser);

        // Given
        given(this.userService.save(any(User.class))).willReturn(newUser);

        // When and then
        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add User Success"))
                .andExpect(jsonPath("$.data.email").value("newUser@hh.se"));
    }

    @Test
    void testUpdatedUser() throws JsonProcessingException {
        User update = new User();
        update.setEmail("user@hh.se");
        update.setRoles("newRole");

        String json = this.objectMapper.writeValueAsString(update);

        System.out.println(json);

        given(this.userService.update("user@hh.se", update)).willReturn(update);

        User returnedUser = this.userService.update("user@hh.se", update);

        Assertions.assertNotNull(returnedUser);
    }

    @Test
    void testUpdateUserSuccess() throws Exception {

        UserDtoForm userDtoForm = new UserDtoForm("newRole"); // Role changes from 'user' to 'admin'

        User updatedUser = new User();
        updatedUser.setEmail("user@hh.se");
        updatedUser.setRoles("newRole");

        String json = this.objectMapper.writeValueAsString(userDtoForm);

        // Given
        given(this.userService.update(eq("user@hh.se"), Mockito.any(User.class))).willReturn(updatedUser);

        // When and then
        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/user@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success"))
                .andExpect(jsonPath("$.data.email").value("user@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("newRole"));
    }

    @Test
    void testUpdateUserWithNonExistentEmail() throws Exception {

        UserDtoForm userDtoForm = new UserDtoForm("newRole");

        String json = this.objectMapper.writeValueAsString(userDtoForm);

        // Given
        given(this.userService.update(eq("abc"), Mockito.any(User.class))).willThrow(new ObjectNotFoundException("user", "abc"));

        // When and then
        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserByIdSuccess() throws Exception {

        // Given
        willDoNothing().given(this.userService).delete("abc");

        // When and then
        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/user@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete User Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserByIdWithNonExistingId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("user", "abc")).when(this.userService).delete("abc");

        // When and then
        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}