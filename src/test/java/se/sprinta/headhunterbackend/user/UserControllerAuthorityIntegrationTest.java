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
import org.springframework.context.annotation.Profile;
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
@ActiveProfiles("test-mysql")
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
                        .with(httpBasic("admin@hh.se",
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
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[1].email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"));
    }

    @Test
    @DisplayName("Check findUserByEmail (GET)")
    void testFindUserByEmailSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/user1@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One User Success"))
                .andExpect(jsonPath("$.data.email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    @DisplayName("Check findUserByEmail with invalid email (GET)")
    void testFindUserByEmailInvalidEmail() throws Exception {
        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/InvalidEmail")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email InvalidEmail"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check findUserByEmail With Insufficient Permission (GET)")
    void testFindUserByEmailNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrlUsers + "/login").with(httpBasic("user1@hh.se", "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String userRoleToken = "Bearer " + json.getJSONObject("data").getString("token");


        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/admin@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userRoleToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("Check register with valid input, admin permission (POST)")
    void testRegisterUserAdminPermissionSuccess() throws Exception {
        User user = new User();
        user.setEmail("user3@hh.se");
        user.setPassword("123");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add User Success"))
                .andExpect(jsonPath("$.data.email").value("user3@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"));

        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[3].email").value("user3@hh.se"))
                .andExpect(jsonPath("$.data[3].roles").value("user"))
                .andExpect(jsonPath("$.data[3].number_of_jobs").value(0));
    }

    @Test
    @DisplayName("Check register with valid input, no token (POST)")
    void testRegisterUserNoTokenSuccess() throws Exception {
        User user = new User();
        user.setEmail("user3@hh.se");
        user.setPassword("123");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add User Success"))
                .andExpect(jsonPath("$.data.email").value("user3@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"));

        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[3].email").value("user3@hh.se"))
                .andExpect(jsonPath("$.data[3].roles").value("user"))
                .andExpect(jsonPath("$.data[3].number_of_jobs").value(0));

    }

    @Test
    @DisplayName("Check register with invalid input, admin permission (POST)")
    void testRegisterUserAdminPermissionInvalidInput() throws Exception {
        User user = new User();
        user.setEmail("");
        user.setPassword("");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.email").value("email is required."))
                .andExpect(jsonPath("$.data.password").value("password is required."));

        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(0));
    }

    @Test
    @DisplayName("Check register with invalid input, no token (POST)")
    void testRegisterUserNoTokenInvalidInput() throws Exception {
        User user = new User();
        user.setEmail("");
        user.setPassword("");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrlUsers + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.email").value("email is required."))
                .andExpect(jsonPath("$.data.password").value("password is required."));

        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(0));
    }

    @Test
    @DisplayName("Check updateUser when updating own user with valid input (PUT)")
    void testUpdateOwnUserSuccess() throws Exception {
        UserDtoForm userDtoForm = new UserDtoForm("user");

        String json = this.objectMapper.writeValueAsString(userDtoForm);

        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/admin@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success"))
                .andExpect(jsonPath("$.data.email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.number_of_jobs").value(0));
    }

    @Test
    @DisplayName("Check updateUser when updating other user with valid input (PUT)")
    void testUpdateOtherUserSuccess() throws Exception {
        UserDtoForm userDtoForm = new UserDtoForm("admin");

        String json = this.objectMapper.writeValueAsString(userDtoForm);

        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/user1@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success"))
                .andExpect(jsonPath("$.data.email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("admin"));
    }

    @Test
    @DisplayName("Check updateUser when updating other user with valid input (PUT)")
    void testUpdateInvalidEmail() throws Exception {
        UserDtoForm userDtoForm = new UserDtoForm("someRole");

        String json = this.objectMapper.writeValueAsString(userDtoForm);

        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/Invalid Email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check updateUser with insufficient permission (PUT)")
    void testUpdateUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrlUsers + "/login").with(httpBasic("user1@hh.se", "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String userRoleToken = "Bearer " + json.getJSONObject("data").getString("token");

        UserDtoForm userDtoForm = new UserDtoForm("newRole");

        String jsonRole = this.objectMapper.writeValueAsString(userDtoForm);

        this.mockMvc.perform(put(this.baseUrlUsers + "/update" + "/admin@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRole)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userRoleToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));

        this.mockMvc.perform(get(this.baseUrlUsers + "/findUser" + "/admin@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One User Success"))
                .andExpect(jsonPath("$.data.email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("admin"))
                .andExpect(jsonPath("$.data.number_of_jobs").value(0));
    }

    @Test
    @DisplayName("Check deleteUser with valid input(DELETE)")
    void testDeleteUserSuccess() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/user1@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete User Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0));

    }

    @Test
    @DisplayName("Check deleteUser with invalid input (DELETE)")
    void testDeleteUserWithNonExistentId() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/Invalid Email")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());

        this.mockMvc.perform(get(this.baseUrlUsers + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[1].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[2].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(0));
    }

    @Test
    @DisplayName("Check deleteUser with insufficient permission (DELETE)")
    void testDeleteUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrlUsers + "/login").with(httpBasic("user1@hh.se", "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String userRoleToken = "Bearer " + json.getJSONObject("data").getString("token");
//        String userEmail = json.getJSONObject("data").getJSONObject("userInfo").getString("email");

        this.mockMvc.perform(delete(this.baseUrlUsers + "/delete" + "/admin@hh.se")
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
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].email").value("admin@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[1].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[2].email").value("user2@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(0));

    }
}