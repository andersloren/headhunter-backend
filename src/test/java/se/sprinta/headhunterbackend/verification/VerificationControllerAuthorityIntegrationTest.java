package se.sprinta.headhunterbackend.verification;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
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
import se.sprinta.headhunterbackend.HeadhunterBackendApplication;
import se.sprinta.headhunterbackend.system.StatusCode;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Verification API endpoints")
@ActiveProfiles("integration-test")
@Transactional
public class VerificationControllerAuthorityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url-account}")
    private String baseUrlAccount;

    @Value("${api.endpoint.base-url-verification}")
    private String baseUrlVerification;
    @Autowired
    private HeadhunterBackendApplication headhunterBackendApplication;

    public String userToken() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrlAccount + "/login")
                        .with(httpBasic(
                                "user1-integrationTest@hh.se",
                                "a")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        return "Bearer " + json.getJSONObject("data").getString("token");
    }

    public String adminToken() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrlAccount + "/login")
                        .with(httpBasic("admin-integrationTest@hh.se",
                                "a")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        return "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("DELETE - delete - Admin Permission - Success")
    void test_Delete_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlVerification + "/delete" + "/" + "user1-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"));
    }

    @Test
    @DisplayName("DELETE - delete - Invalid Email - Exception")
    void test_Delete_InvalidEmail_Exception() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlVerification + "/delete" + "/" + "Invalid Email")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find verification with Id Invalid Email"));
    }

    @Test
    @DisplayName("DELETE - delete - User No Permission - Exception")
    void test_Delete_UserNoPermission_Exception() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlVerification + "/delete" + "/" + "user1-integrationTest@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No Permission"));
    }

}
