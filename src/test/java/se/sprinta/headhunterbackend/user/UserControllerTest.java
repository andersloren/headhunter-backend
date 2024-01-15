package se.sprinta.headhunterbackend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Turn off Spring security
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<User> users;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

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

        this.users = new ArrayList<>();
        this.users.add(u1);
        this.users.add(u2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        // Given

        given(this.userService.findAll()).willReturn(this.users);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].username").value("Mikael"))
                .andExpect(jsonPath("$.data[0].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[1].username").value("Anders"))
                .andExpect(jsonPath("$.data[1].email").value("a@l.se"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        // Given
        given(this.userService.findByUserId("a@l.se")).willReturn(this.users.get(1));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/users/a@l.se").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.email").value("a@l.se"))
                .andExpect(jsonPath("$.data.username").value("Anders"));
    }

    @Test
    void testFindUserByIdWithNonExistentId() throws Exception {
        // Given
        given(this.userService.findByUserId("abc")).willThrow(new ObjectNotFoundException("user", "abc"));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/users/abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddUserSuccess() throws Exception {
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
        this.mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.username").value("Mehrdad Javan"));
    }

    @Test
    void testDeleteUserByIdSuccess() throws Exception {
        // Given
        doNothing().when(this.userService).delete("a@l.se");

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/users/a@l.se").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserByIdWithNonExistingId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("user", "abc")).when(this.userService).delete("abc");

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/users/abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}