package se.sprinta.headhunterbackend.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
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
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormUpdate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Account API endpoints")
@ActiveProfiles("integration-test")
@Transactional
public class AccountControllerAuthorityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url-account}")
    private String baseUrlAccount;

    public String userToken() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrlAccount + "/login")
                        .with(httpBasic(
                                "user1-integrationTest@hh.se",
                                "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        return "Bearer " + json.getJSONObject("data").getString("token");
    }

    public String adminToken() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrlAccount + "/login")
                        .with(httpBasic("admin-integrationTest@hh.se",
                                "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        return "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("GET - findAll - Admin Permission - Success")
    void test_FindAll_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].email").value("admin-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[0].password").isNotEmpty())
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[1].password").isNotEmpty())
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[1].number_of_jobs").value(2))
                .andExpect(jsonPath("$.data[2].email").value("user2-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[2].password").isNotEmpty())
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(1))
                .andExpect(jsonPath("$.data[3].email").value("user3-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[3].password").isNotEmpty())
                .andExpect(jsonPath("$.data[3].roles").value("user"))
                .andExpect(jsonPath("$.data[3].number_of_jobs").value(1));
    }

    @Test
    @DisplayName("GET - findAll - User No Permission - Exception")
    void test_FindAll_UserNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - Admin Permission - Non-Existing Email - Success")
    void test_validateEmailAvailable_AdminPermission_NonExistingEmail_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/availableEmail@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Email is available"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - User Permission - Non-Existing Email - Success")
    void test_ValidateEmailAvailable_UserPermission_NonExistingEmail_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/availableEmail@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Email is available"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - No Authorization - Non-Existing Email - Success")
    void test_ValidateEmailAvailable_NoAuthorization_NonExistingEmail_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/availableEmail@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Email is available"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - Admin Permission - Existing Email - Exception")
    void test_ValidateEmailAvailable_AdminPermission_ExistingEmail_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/admin-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.CONFLICT))
                .andExpect(jsonPath("$.message").value("admin-integrationTest@hh.se is already registered"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - User Permission - Existing Email - Exception")
    void test_ValidateEmailAvailable_UserPermission_ExistingEmail_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/admin-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.CONFLICT))
                .andExpect(jsonPath("$.message").value("admin-integrationTest@hh.se is already registered"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - No Authorization - Existing Email - Exception")
    void test_ValidateEmailAvailable_NoAuthorization_ExistingEmail_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/admin-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.CONFLICT))
                .andExpect(jsonPath("$.message").value("admin-integrationTest@hh.se is already registered"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - Admin Permission - Success")
    void test_GetAccountDtoByEmail_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/user1-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Account Dto Success"))
                .andExpect(jsonPath("$.data.email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.number_of_jobs").value(2));
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - Admin Permission - Invalid email - Exception")
    void test_GetAccountDtoByEmail_AdminPermission_InvalidEmail_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/Invalid Email")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - User Permission - Success")
    void test_GetAccountDtoByEmail_UserNoPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/user1-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.message").value("Find Account Dto Success"))
                .andExpect(jsonPath("$.data.email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.number_of_jobs").value(2));
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - User Permission - Invalid email - Exception")
    void test_GetAccountDtoByEmail_UserPermission_InvalidEmail_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/Invalid Email")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("POST - register - Admin Permission - Exception")
    void test_RegisterAccount_AdminNoPermission_Exception() throws Exception {
        AccountDtoFormRegister accountDtoFormRegister = new AccountDtoFormRegister(
                "user4-integrationTest@hh.se",
                "a");

        String json = this.objectMapper.writeValueAsString(accountDtoFormRegister);

        this.mockMvc.perform(post(this.baseUrlAccount + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Account Success"))
                .andExpect(jsonPath("$.data.email").value("user4-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.number_of_jobs").value(0));
    }

    @Test
    @DisplayName("POST - register - User Permission - Success")
    void test_Register_UserPermission_Success() throws Exception {
        AccountDtoFormRegister newAccount = new AccountDtoFormRegister(
                "user4-integrationTest@hh.se",
                "a");

        String json = this.objectMapper.writeValueAsString(newAccount);

        this.mockMvc.perform(post(this.baseUrlAccount + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Account Success"))
                .andExpect(jsonPath("$.data.email").value("user4-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.number_of_jobs").value(0));

        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.data[0].email").value("admin-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[0].password").isNotEmpty())
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[1].password").isNotEmpty())
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[1].number_of_jobs").value(2))
                .andExpect(jsonPath("$.data[2].email").value("user2-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[2].password").isNotEmpty())
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(1))
                .andExpect(jsonPath("$.data[3].email").value("user3-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[3].password").isNotEmpty())
                .andExpect(jsonPath("$.data[3].roles").value("user"))
                .andExpect(jsonPath("$.data[3].number_of_jobs").value(1))
                .andExpect(jsonPath("$.data[4].email").value("user4-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[4].password").isNotEmpty())
                .andExpect(jsonPath("$.data[4].roles").value("user"))
                .andExpect(jsonPath("$.data[4].number_of_jobs").value(0));
    }

    @Test
    @DisplayName("POST - register - User Permission - Invalid Input - Exception")
    void test_Register_UserPermission_InvalidInput_Exception() throws Exception {
        AccountDtoFormRegister accountDtoFormRegister = new AccountDtoFormRegister(
                "",
                "");

        String json = this.objectMapper.writeValueAsString(accountDtoFormRegister);

        this.mockMvc.perform(post(this.baseUrlAccount + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.email").value("email is required"))
                .andExpect(jsonPath("$.data.password").value("password is required"));

        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].email").value("admin-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[0].password").isNotEmpty())
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[1].password").isNotEmpty())
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[1].number_of_jobs").value(2))
                .andExpect(jsonPath("$.data[2].email").value("user2-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[2].password").isNotEmpty())
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(1))
                .andExpect(jsonPath("$.data[3].email").value("user3-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[3].password").isNotEmpty())
                .andExpect(jsonPath("$.data[3].roles").value("user"))
                .andExpect(jsonPath("$.data[3].number_of_jobs").value(1));
    }

    @Test
    @DisplayName("PUT - update - Admin Permission - Success")
    void test_Update_AdminPermission_Success() throws Exception {
        AccountDtoFormUpdate accountDtoFormUpdate = new AccountDtoFormUpdate("admin");

        String json = this.objectMapper.writeValueAsString(accountDtoFormUpdate);

        this.mockMvc.perform(put(this.baseUrlAccount + "/update" + "/user1-integrationTest@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Account Success"))
                .andExpect(jsonPath("$.data.email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("admin"));
    }

    @Test
    @DisplayName("PUT - update - User No Permission - Exception")
    void test_Update_UserNoPermission_Exception() throws Exception {

        AccountDtoFormUpdate accountDtoFormUpdate = new AccountDtoFormUpdate("newRole");

        String jsonRole = this.objectMapper.writeValueAsString(accountDtoFormUpdate);

        this.mockMvc.perform(put(this.baseUrlAccount + "/update" + "/admin-integrationTest@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRole)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));

        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/admin-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Account Dto Success"))
                .andExpect(jsonPath("$.data.email").value("admin-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("admin"));
    }

    @Test
    @DisplayName("DELETE - delete - Admin Permission - Success")
    void test_Delete_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlAccount + "/delete" + "/user1-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Account Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].email").value("admin-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[0].password").isNotEmpty())
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user2-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[1].password").isNotEmpty())
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(1))
                .andExpect(jsonPath("$.data[2].email").value("user3-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[2].password").isNotEmpty())
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(1));
    }

    @Test
    @DisplayName("DELETE - delete - Invalid Input - Exception")
    void test_Delete_AdminPermission_InvalidInput_Exception() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlAccount + "/delete" + "/Invalid Email")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());

        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].email").value("admin-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[1].email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].email").value("user2-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[3].email").value("user3-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[3].roles").value("user"));
    }

    @Test
    @DisplayName("DELETE - delete - User No Permission - Exception")
    void test_DeleteAccount_UserNoPermission_Exception() throws Exception {

        this.mockMvc.perform(delete(this.baseUrlAccount + "/delete" + "/admin-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));

        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].email").value("admin-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[1].email").value("user1-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[2].email").value("user2-integrationTest@hh.se"))
                .andExpect(jsonPath("$.data[2].roles").value("user"));
    }
}
