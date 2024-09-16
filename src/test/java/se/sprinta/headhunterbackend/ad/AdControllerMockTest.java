package se.sprinta.headhunterbackend.ad;

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
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("mock-test")
@WebMvcTest(AdController.class)
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class AdControllerMockTest {

    @MockBean
    private AdService adService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url-ad}")
    String baseUrlAd;

    @Value("${ai.openai.model}")
    private String model;

    List<AccountDtoView> accountDtos = new ArrayList<>();
    List<Ad> ads = new ArrayList<>();
    List<AdDtoView> adDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.ads = MockDatabaseInitializer.initializeMockAds();
        this.accountDtos = MockDatabaseInitializer.initializeMockAccountDtos();
        this.adDtos = MockDatabaseInitializer.initializeMockAdDtos();
    }

    @Test
    @DisplayName("Test Data Initializer")
    void test_DataInitializer() {
        System.out.println("AdControllerMockTest, ads size: " + this.ads.size());
        for (Ad ad : this.ads) {
            System.out.println(ad.toString());
        }
        System.out.println("AdControllerMockTest, adDtos size: " + this.adDtos.size());
        for (AdDtoView adDto : this.adDtos) {
            System.out.println(adDto.toString());
        }
    }

    @Test
    @DisplayName("GET - findAll - Success")
    void test_FindAll_Success() throws Exception {
        // Given
        given(this.adService.findAll()).willReturn(this.ads);

        ResultActions result = this.mockMvc.perform(get(this.baseUrlAd + "/findAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Ads Success"));

        JsonNode dataArray = TestUtils.getJSONResponse(result);

        for (int i = 0; i < dataArray.size(); i++) {
            result.andExpect(jsonPath("$.data[" + i + "].id").value(this.ads.get(i).getId()))
                    .andExpect(jsonPath("$.data[" + i + "].dateCreated").value(this.ads.get(i).getDateCreated().toString()))
                    .andExpect(jsonPath("$.data[" + i + "].htmlCode").value(this.ads.get(i).getHtmlCode()))
                    .andExpect(jsonPath("$.data[" + i + "].job.id").value(this.ads.get(i).getJob().getId()))
                    .andExpect(jsonPath("$.data[" + i + "].job.title").value(this.ads.get(i).getJob().getTitle()))
                    .andExpect(jsonPath("$.data[" + i + "].job.description").value(this.ads.get(i).getJob().getDescription()))
                    .andExpect(jsonPath("$.data[" + i + "].job.instruction").value(this.ads.get(i).getJob().getInstruction()))
                    .andExpect(jsonPath("$.data[" + i + "].job.recruiterName").value(this.ads.get(i).getJob().getRecruiterName()))
                    .andExpect(jsonPath("$.data[" + i + "].job.adCompany").value(this.ads.get(i).getJob().getAdCompany()))
                    .andExpect(jsonPath("$.data[" + i + "].job.adEmail").value(this.ads.get(i).getJob().getAdEmail()))
                    .andExpect(jsonPath("$.data[" + i + "].job.adPhone").value(this.ads.get(i).getJob().getAdPhone()))
                    .andExpect(
                            jsonPath("$.data[" + i + "].job.applicationDeadline")
                                    .value(this.ads.get(i).getJob().getApplicationDeadline()))
                    .andExpect(
                            jsonPath("$.data[" + i + "].job.account.email").value(this.ads.get(i).getJob().getAccount().getEmail()))
                    .andExpect(
                            jsonPath("$.data[" + i + "].job.account.roles").value(this.ads.get(i).getJob().getAccount().getRoles()))
                    .andExpect(jsonPath("$.data[" + i + "].job.account.number_of_jobs")
                            .value(this.ads.get(i).getJob().getAccount().getNumber_of_jobs()));
        }
    }

    @Test
    @DisplayName("GET - findById - Success")
    void test_FindById_Success() throws Exception {
        // Given
        given(this.adService.findById("id 1")).willReturn(this.ads.get(0));

        // When and Then
        this.mockMvc.perform(get(this.baseUrlAd + "/findById" + "/id 1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Ad Success"))
                .andExpect(jsonPath("$.data.id").value(this.ads.get(0).getId()))
                .andExpect(jsonPath("$.data.htmlCode").value(this.ads.get(0).getHtmlCode()))
                .andExpect(jsonPath("$.data.dateCreated").value(this.ads.get(0).getDateCreated().toString()))
                .andExpect(jsonPath("$.data.job.id").value(this.ads.get(0).getJob().getId()))
                .andExpect(jsonPath("$.data.job.description").value(this.ads.get(0).getJob().getDescription()))
                .andExpect(jsonPath("$.data.job.instruction").value(this.ads.get(0).getJob().getInstruction()))
                .andExpect(jsonPath("$.data.job.recruiterName").value(this.ads.get(0).getJob().getRecruiterName()))
                .andExpect(jsonPath("$.data.job.adCompany").value(this.ads.get(0).getJob().getAdCompany()))
                .andExpect(jsonPath("$.data.job.adEmail").value(this.ads.get(0).getJob().getAdEmail()))
                .andExpect(jsonPath("$.data.job.adPhone").value(this.ads.get(0).getJob().getAdPhone()))
                .andExpect(jsonPath("$.data.job.applicationDeadline").value(this.ads.get(0).getJob().getApplicationDeadline()))
                .andExpect(jsonPath("$.data.job.account.email").value(this.ads.get(0).getJob().getAccount().getEmail()))
                .andExpect(jsonPath("$.data.job.account.roles").value(this.ads.get(0).getJob().getAccount().getRoles()))
                .andExpect(jsonPath("$.data.job.account.number_of_jobs")
                        .value(this.ads.get(0).getJob().getAccount().getNumber_of_jobs()));
    }

    @Test
    @DisplayName("GET - findById - Invalid Ad Id - Exception")
    void test_FindById_InvalidAdId_Exception() throws Exception {
        // Given
        given(this.adService.findById("Invalid Id")).willThrow(new ObjectNotFoundException("ad", "Invalid Id"));

        // When and Then
        this.mockMvc.perform(get(this.baseUrlAd + "/findById" + "/Invalid Id")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find ad with Id Invalid Id"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - Invalid Job Id - Exception")
    void testGetAdDtosByJobId_InvalidAdId_Exception() throws Exception {
        // Given
        given(this.adService.getAdDtosByJobId(Long.MAX_VALUE))
                .willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and Then
        this.mockMvc.perform(get(this.baseUrlAd + "/getAdDtosByJobId" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - Success")
    void test_GetAdDtosByJobId_Success() throws Exception {

        this.adDtos.remove(2);

        // Given
        given(this.adService.getAdDtosByJobId(1L)).willReturn(this.adDtos);

        // When and Then
        ResultActions result = this.mockMvc.perform(get(this.baseUrlAd + "/getAdDtosByJobId/" + "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Get Ad Dtos by Job Id Success"));

        JsonNode dataArray = TestUtils.getJSONResponse(result);

        for (int i = 0; i < dataArray.size(); i++) {
            result.andExpect(jsonPath("$.data[" + i + "].id").value(this.adDtos.get(i).id()))
                    .andExpect(jsonPath("$.data[" + i + "].dateCreated").value(this.adDtos.get(i).dateCreated().toString()))
                    .andExpect(jsonPath("$.data[" + i + "].htmlCode").value(this.adDtos.get(i).htmlCode()));
        }
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - Invalid Job Id - Exception")
    void test_GetAdDtosByJobId_InvalidAdId_Exception() throws Exception {
        // Given
        given(this.adService.getAdDtosByJobId(Long.MAX_VALUE))
                .willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and Then
        this.mockMvc.perform(get(this.baseUrlAd + "/getAdDtosByJobId" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - Success")
    void test_GetNumberOfAdsByJobId_Success() throws Exception {
        // Given
        given(this.adService.getNumberOfAdsByJobId(1L)).willReturn(2L);

        // When and Then
        this.mockMvc.perform(get(this.baseUrlAd + "/getNumberOfAdsByJobId" + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Get Number Of Ads by Job Id Success"))
                .andExpect(jsonPath("$.data").value(2L));
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - Invalid Job Id - Exception")
    void test_GetNumberOfAdsByJobId_InvalidAdId_Exception() throws Exception {
        // Given
        given(this.adService.getNumberOfAdsByJobId(Long.MAX_VALUE))
                .willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and Then
        this.mockMvc.perform(get(this.baseUrlAd + "/getNumberOfAdsByJobId" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - getAccountDtoByAdId - Success")
    void test_GetAccountDtoByAdId_Success() throws Exception {
        // Given
        given(this.adService.getAccountDtoByAdId("id 1")).willReturn(this.accountDtos.get(0));

        // When and Then
        this.mockMvc.perform(get(this.baseUrlAd + "/getAccountDtoByAdId" + "/id 1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Get Account Dto By Ad Id Success"))
                .andExpect(jsonPath("$.data.email").value(this.accountDtos.get(0).email()))
                .andExpect(jsonPath("$.data.roles").value(this.accountDtos.get(0).roles()))
                .andExpect(jsonPath("$.data.number_of_jobs").value(this.accountDtos.get(0).number_of_jobs()));
    }

    @Test
    @DisplayName("GET - getAccountDtoByAdId - Invalid Ad Id - Exception")
    void test_GetAccountDtoByAdId_InvalidAdId_Exception() throws Exception {
        // Given
        given(this.adService.getAccountDtoByAdId("abc"))
                .willThrow(new ObjectNotFoundException("ad", "abc"));

        // When and then
        this.mockMvc.perform(get(this.baseUrlAd + "/getAccountDtoByAdId" + "/abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find ad with Id abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("POST - addAd - Success")
    void test_AddAd_Success() throws Exception {
        AdDtoForm newAd = new AdDtoForm(
                "htmlCode 4");

        Ad savedAd = new Ad();
        savedAd.setHtmlCode("htmlCode 4");

        String json = this.objectMapper.writeValueAsString(newAd);

        // Given
        given(this.adService.addAd(1L, newAd)).willReturn(savedAd);

        // When and Then
        this.mockMvc.perform(post(this.baseUrlAd + "/addAd" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Save Ad Success"))
                .andExpect(jsonPath("$.data.id").value(savedAd.getId()))
                .andExpect(jsonPath("$.data.htmlCode").value(savedAd.getHtmlCode()))
                .andExpect(jsonPath("$.data.dateCreated").value(savedAd.getDateCreated().toString()));
    }

    @Test
    @DisplayName("POST - addAd - Invalid Job Id - Exception")
    void test_AddAd_InvalidJobId_Exception() throws Exception {
        AdDtoForm newAd = new AdDtoForm(
                "htmlCode 4");

        String json = this.objectMapper.writeValueAsString(newAd);

        // Given
        given(this.adService.addAd(Long.MAX_VALUE, newAd)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and Then
        this.mockMvc.perform(post(this.baseUrlAd + "/addAd" + "/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE - delete - Success")
    void test_Delete_Success() throws Exception {
        // Given
        doNothing().when(this.adService).delete("id 1");

        // When and Then
        this.mockMvc.perform(delete(this.baseUrlAd + "/delete" + "/id 1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Ad Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE - delete - Invalid Ad Id - Exception")
    void test_Delete_InvalidAdId_Exception() throws Exception {
        //
        doThrow(new ObjectNotFoundException("job", "Invalid Id")).when(this.adService).delete("Invalid Id");

        // When and Then
        this.mockMvc.perform(delete(this.baseUrlAd + "/delete" + "/Invalid Id")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id Invalid Id"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
