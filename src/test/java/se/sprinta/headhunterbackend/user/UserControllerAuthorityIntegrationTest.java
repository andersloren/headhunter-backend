package se.sprinta.headhunterbackend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.dto.UserDtoForm;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for User API endpoints")
@Tag("integration")
@ActiveProfiles("dev")
@Transactional
public class UserControllerAuthorityIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url-users}")
    String baseUrlUsers;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrlUsers + "/login")
                        .with(httpBasic("m@e.se",
                                "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("Check findAllUsers (GET)")
    void testFindAllUsers() throws Exception {
        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].email").value("a@l.se"))
                .andExpect(jsonPath("$.data[0].roles").value("user"))
                .andExpect(jsonPath("$.data[0].numberOfJobs").value(2))
                .andExpect(jsonPath("$.data[1].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[1].roles").value("admin user"));
    }

    @Test
    @DisplayName("Check deleteUser with insufficient permission (DELETE)")
    void testFindAllUsersNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrlUsers + "/login").with(httpBasic("a@l.se", "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String userRoleToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userRoleToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("Check findUserByEmail (GET)")
    void testFindUserByEmail() throws Exception {
        String existingEmail = "a@l.se";
        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/" + existingEmail)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One User Success"))
                .andExpect(jsonPath("$.data.email").value("a@l.se"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    // TODO: 31/01/2024 testFindUserWithNonExistentEmail

    // TODO: 31/01/2024 testFindUserNoAccessAsRoleUser

    @Test
    @DisplayName("Check addUser with valid input (POST)")
    void testAddUserSuccess() throws Exception {
        User user = new User();
        user.setEmail("m@j.se");
        user.setPassword("02468");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add User Success"))
                .andExpect(jsonPath("$.data.email").value("m@j.se"));
    }

    @Test
    @DisplayName("Check addUser with invalid input (POST)")
    void testAddUserInvalidInput() throws Exception {
        User user = new User();
        user.setPassword("");
        user.setRoles("");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.username").value("username is required."))
                .andExpect(jsonPath("$.data.password").value("password is required."));
    }

    // TODO: 31/01/2024 testAddUserNoAccessAsRoleUser

    @Test
    @DisplayName("Check updateUser when updating own user with valid input (PUT)")
    void testUpdateOwnUserSuccess() throws Exception {
        String email = "m@e.se";

        UserDtoForm userDtoForm = new UserDtoForm( "admin");

        String json = this.objectMapper.writeValueAsString(userDtoForm);

        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success"))
                .andExpect(jsonPath("$.data.email").value("m@e.se"))
                .andExpect(jsonPath("$.data.username").value("Mikael - updated"))
                .andExpect(jsonPath("$.data.roles").value("admin"));
    }

    @Test
    @DisplayName("Check updateUser when updating other user with valid input (PUT)")
    void testUpdateOtherUserSuccess() throws Exception {
        String otherEmail = "a@l.se";

        UserDtoForm userDtoForm = new UserDtoForm( "admin");

        String json = this.objectMapper.writeValueAsString(userDtoForm);

        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/" + otherEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success"))
                .andExpect(jsonPath("$.data.email").value("a@l.se"))
                .andExpect(jsonPath("$.data.roles").value("admin"));
    }

    // TODO: 31/01/2024 testUpdateWithNonExistentEmail

    @Test
    @DisplayName("Check updateUser with insufficient permission (PUT)")
    void testUpdateUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrlUsers + "/login").with(httpBasic("a@l.se", "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String userRoleToken = "Bearer " + json.getJSONObject("data").getString("token");

        String email = "a@l.se";
        String roles = "admin";

        String jsonRole = this.objectMapper.writeValueAsString(roles);

        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRole)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userRoleToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/" + email)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One User Success"))
                .andExpect(jsonPath("$.data.email").value("a@l.se"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    @DisplayName("Check updateUser with invalid input (PUT)")
    void testUpdateWithInvalidInput() {
        // Fix this later.
        // Add test for updatingOther user?
    }

    @Test
    @DisplayName("Check deleteUser with valid input(DELETE)")
    void testDeleteUserSuccess() throws Exception {
        String existingEmail = "a@l.se";

        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/" + existingEmail)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete User Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteUser with invalid input (DELETE)")
    void testDeleteUserWithNonExistentId() throws Exception {
        String nonExistentEmail = "abc";

        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/" + nonExistentEmail)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteUser with insufficient permission (DELETE)")
    void testDeleteUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrlUsers + "/login").with(httpBasic("a@l.se", "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String userRoleToken = "Bearer " + json.getJSONObject("data").getString("token");
        String userEmail = json.getJSONObject("data").getJSONObject("userInfo").getString("email");

        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/" + userEmail)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userRoleToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].email").value("a@l.se"))
                .andExpect(jsonPath("$.data[0].roles").value("user"))
                .andExpect(jsonPath("$.data[1].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[1].roles").value("admin user"));

    }
}