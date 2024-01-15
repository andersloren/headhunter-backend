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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for User API endpoints")
@Tag("integration")
@ActiveProfiles(value = "dev")
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrl + "/users/login")
                        .with(httpBasic("m@e.se",
                                "123456"))); // httpBasic() is from spring-security-test.
        System.out.println("resultActions: " + resultActions.toString());
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        System.out.println("mvcResult: " + mvcResult);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("contentAsString: " + contentAsString);
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("Check findAllUsers (GET)")
    void testFindAllUsers() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[0].username").value("Mikael"))
                .andExpect(jsonPath("$.data[1].email").value("a@l.se"))
                .andExpect(jsonPath("$.data[1].username").value("Anders"));
    }

    @Test
    @DisplayName("Check findUserById (GET)")
    void testFindUserByEmail() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("a@l.se", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String andersToken = "Bearer " + json.getJSONObject("data").getString("token");
        String andersEmail = json.getJSONObject("data").getJSONObject("userInfo").getString("email");
        String andersEmailUri = "/" + andersEmail;

        this.mockMvc.perform(get(this.baseUrl + "/users" + andersEmailUri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, andersToken))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.email").value(andersEmail))
                .andExpect(jsonPath("$.data.email").value("a@l.se"))
                .andExpect(jsonPath("$.data.username").value("Anders"));
    }

    @Test
    @DisplayName("Check deleteUser with insufficient permission (DELETE)")
    void testFindAllUsersNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("a@l.se", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String andersToken = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(get(this.baseUrl + "/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, andersToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("Check addUser with valid input (POST)")
    void testAddUserSuccess() throws Exception {
        User user = new User();
        user.setEmail("m@j.se");
        user.setUsername("Mehrdad");
        user.setPassword("02468");
        user.setRoles("admin");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.username").value("Mehrdad"))
                .andExpect(jsonPath("$.data.email").value("m@j.se"));
    }

    @Test
    @DisplayName("Check addUser with invalid input (POST)")
    void testAddUserInvalidInput() throws Exception {
        User user = new User();
        user.setUsername("");
        user.setPassword("");
        user.setRoles("");

        String json = this.objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(this.baseUrl + "/users")
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

    //update success (String userId, User update)


    //delete success
    //delete invalid input

    @Test
    @DisplayName("Check deleteUser with insufficient permission (DELETE)")
    void testDeleteUserNoAccessAsRoleUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("a@l.se", "654321"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String andersToken = "Bearer " + json.getJSONObject("data").getString("token");
        String andersEmail = json.getJSONObject("data").getJSONObject("userInfo").getString("email");
        String andersEmailUri = "/" + andersEmail;

        this.mockMvc.perform(delete(this.baseUrl + "/users" + andersEmailUri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, andersToken))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
        this.mockMvc.perform(get(this.baseUrl + "/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[0].username").value("Mikael"))
                .andExpect(jsonPath("$.data[1].email").value("a@l.se"))
                .andExpect(jsonPath("$.data[1].username").value("Anders"));
    }

    @Test
    @DisplayName("Check deleteUser with valid input(DELETE)")
    void testDeleteUserSuccess() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/a@l.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check deleteUser with invalid input (DELETE)")
    void testDeleteUserWithNonExistentId() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/abc")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}