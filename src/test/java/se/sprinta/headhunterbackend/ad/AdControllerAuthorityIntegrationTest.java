package se.sprinta.headhunterbackend.ad;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
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
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.system.StatusCode;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Ad API endpoints")
@ActiveProfiles("integration-test")
@Transactional
public class AdControllerAuthorityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url-account}")
    private String baseUrlAccount;

    @Value("${api.endpoint.base-url-ad}")
    private String baseUrlAd;

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

    /**
     * Finds the id for the first Ad object when running a "findAll".
     * This is helpful since Ad has UUID id and can not be guessed.
     *
     * @return returns the first id of all ads.
     */
    public String getAdId() {
        try {
            ResultActions resultActions = this.mockMvc.perform(get(this.baseUrlAd + "/findAll")
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, adminToken()));

            MvcResult mvcResult = resultActions.andDo(print()).andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            JsonNode rootNode = objectMapper.readTree(contentAsString);
            return rootNode.path("data").get(0).path("id").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong, check message";
        }
    }

    @Test
    @DisplayName("GET - findAll - Admin Permission - Success")
    void test_FindAll_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Ads Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("GET - findAll - User No Permission - Exception")
    void test_FindAll_UserNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("GET - findById - Admin Permission - Success")
    void test_FindById_AdminPermission_Success() throws Exception {
        String adId = getAdId();

        this.mockMvc.perform(get(this.baseUrlAd + "/findById" + "/" + adId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Ad Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @DisplayName("GET - findById - User No Permission - Exception")
    void test_FindById_UserNoPermission_Exception() throws Exception {
        String adId = getAdId();

        this.mockMvc.perform(get(this.baseUrlAd + "/findById" + "/" + adId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("GET - getAdsByJobId - Admin No Permission - Exception")
    void test_GetAdsByJobId_AdminNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/getAdsByJobId" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - Admin No Permission - Exception")
    void test_GetAdDtosByJobId_AdminNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/getAdDtosByJobId" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - User Permission - Success")
    void test_GetAdDtosByJobId_UserPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/getAdDtosByJobId" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Get Ad Dtos by Job Id Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - User Permission - Invalid Job Id - Exception")
    void test_GetAdDtosByJobId_UserPermission_InvalidJobId_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/getAdDtosByJobId" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - Admin No Permission - Exception")
    void test_GetNumberOfAdsByJobId_AdminNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/getNumberOfAdsByJobId" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - User Permission - Success")
    void test_GetNumberOfAdsByJobId_UserPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/getNumberOfAdsByJobId" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Get Number Of Ads by Job Id Success"))
                .andExpect(jsonPath("$.data").value(2));
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - User Permission - Invalid Job Id - Exception")
    void test_GetNumberOfAdsByJobId_UserPermission_InvalidJobId_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAd + "/getNumberOfAdsByJobId" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getAccountDtoByAdId - Admin No Permission - Exception")
    void test_GetAccountDtoByAdId_AdminNoPermission_Exception() throws Exception {
        String adId = getAdId();

        this.mockMvc.perform(get(this.baseUrlAd + "/getAccountDtoByAdId" + "/" + adId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("GET - getAccountDtoByAdId - User Permission - Success")
    void test_GetAccountDtoByAdId_UserPermission_Success() throws Exception {
        String adId = getAdId();

        this.mockMvc.perform(get(this.baseUrlAd + "/getAccountDtoByAdId" + "/" + adId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Get Account Dto By Ad Id Success"))
                .andExpect(jsonPath("$.data.email").isNotEmpty())
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.number_of_jobs").isNotEmpty());
    }

    @Test
    @DisplayName("POST - adAdd - Admin No Permission - Exception")
    void test_AddAd_AdminNoPermission_Exception() throws Exception {
        AdDtoForm newAd = new AdDtoForm(
                "htmlCode 4");

        String json = this.objectMapper.writeValueAsString(newAd);

        this.mockMvc.perform(post(this.baseUrlAd + "/addAd" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("POST - addAd - User Permission - Success")
    void test_AddAd_UserPermission_Success() throws Exception {
        AdDtoForm newAd = new AdDtoForm(
                "htmlCode 4");

        String json = this.objectMapper.writeValueAsString(newAd);

        this.mockMvc.perform(post(this.baseUrlAd + "/addAd" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Save Ad Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.htmlCode").value("htmlCode 4"))
                .andExpect(jsonPath("$.data.dateCreated").isNotEmpty());
    }

    @Test
    @DisplayName("addAd - User Permission - Invalid Job Id - Exception")
    void test_AddAd_UserPermission_InvalidJobId_Exception() throws Exception {
        AdDtoForm newAd = new AdDtoForm(
                "htmlCode 4");

        String json = this.objectMapper.writeValueAsString(newAd);

        this.mockMvc.perform(post(this.baseUrlAd + "/addAd" + "/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE - addAd - Admin No Permission - Exception")
    void test_Delete_AdminNoPermission_Exception() throws Exception {
        String adId = getAdId();

        this.mockMvc.perform(delete(this.baseUrlAd + "/delete" + "/" + adId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("DELETE - delete - User Permission - Success")
    void test_Delete_UserPermission_Success() throws Exception {
        String adId = getAdId();

        this.mockMvc.perform(delete(this.baseUrlAd + "/delete" + "/" + adId)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Ad Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE - delete - User Permission - Invalid Ad Id - Exception")
    void test_Delete_UserPermission_InvalidJobId_Exception() throws Exception {
        String adId = getAdId();

        this.mockMvc.perform(delete(this.baseUrlAd + "/delete" + "/Invalid Id")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find ad with Id Invalid Id"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
