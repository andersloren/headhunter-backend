package se.sprinta.headhunterbackend.verification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.TestUtils;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("mock-test")
@WebMvcTest(VerificationController.class)
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class VerificationControllerMockTest {

    @MockBean
    private VerificationService verificationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url-verification}")
    String baseUrlVerification;

    private List<Verification> verifications = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.verifications = MockDatabaseInitializer.initializeMockVerifications();
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
    }

    @Test
    @DisplayName("Test Data Initializer")
    void test_DataInitializer() {
        System.out.println("VerificationControllerMockTest, accounts size: " + this.verifications.size());
        for (Verification verification : this.verifications) {
            System.out.println(verification.toString());
        }
    }

    @Test
    @DisplayName("GET - Find All - Success")
    void test_FindAll_Success() throws Exception {
        // Given
        given(this.verificationService.findAll()).willReturn(this.verifications);

        // When and then
        ResultActions result = this.mockMvc.perform(get(this.baseUrlVerification + "/findAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Verifications Success"));

        JsonNode dataArray = TestUtils.getJSONResponse(result);

        for (int i = 0; i < dataArray.size(); i++) {
            result.andExpect(jsonPath("$.data[" + i + "].id").value(this.verifications.get(i).getId()))
                    .andExpect(jsonPath("$.data[" + i + "].verificationCode").value(this.verifications.get(i).getVerificationCode()))
                    .andExpect(jsonPath("$.data[" + i + "].account.email").value(this.verifications.get(i).getAccount().getEmail()))
                    .andExpect(jsonPath("$.data[" + i + "].account.roles").value(this.verifications.get(i).getAccount().getRoles()))
                    .andExpect(jsonPath("$.data[" + i + "].account.number_of_jobs").value(this.verifications.get(i).getAccount().getNumber_of_jobs()))
                    .andExpect(jsonPath("$.data[" + i + "].account.verified").value(this.verifications.get(i).getAccount().isVerified()));
        }
    }

    @Test
    @DisplayName("POST - Verify Registration - Success")
    void test_VerifyRegistration_Success() throws Exception {
        // Given
        willDoNothing().given(this.verificationService).verifyRegistration(this.accounts.get(0).getEmail(), this.verifications.get(0).getVerificationCode());

        // When and then
        this.mockMvc.perform(post(this.baseUrlVerification + "/verifyRegistration" + "/" + this.accounts.get(0).getEmail() + "/" + this.verifications.get(0).getVerificationCode())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Verification Success"));

    }
}