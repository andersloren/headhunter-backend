package se.sprinta.headhunterbackend.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
@DisplayName("Integration tests for Ad API endpoints")
@Tag("integration")
@ActiveProfiles("dev")
@Transactional
public class AdControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url-ads}")
    String baseUrlAds;

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
    @DisplayName("Check findUserByAdId (GET)")
    void testFindUserByAdId() throws Exception {
        this.mockMvc.perform(get(this.baseUrlAds + "/findUserByAdId/48cf64a6-f32a-4311-9714-2c0f3567ebde")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find User By Ad Id Success"))
                .andExpect(jsonPath("$.data.email").value("m@e.se"))
                .andExpect(jsonPath("$.data.username").value("Mikael"))
                .andExpect(jsonPath("$.data.roles").value("admin user"))
                .andExpect(jsonPath("$.data.numberOfJobs").value(3));
    }

}
