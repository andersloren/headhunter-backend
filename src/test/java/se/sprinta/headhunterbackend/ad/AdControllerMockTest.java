package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.config.JacksonConfig;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AdController.class)
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
@Import(JacksonConfig.class)
class AdControllerMockTest {

    @MockBean
    private AdService adService;

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.endpoint.base-url-ad}")
    String baseUrlAd;

    List<Ad> ads = new ArrayList<>();
    List<AdDtoView> adDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.ads = MockDatabaseInitializer.initializeMockAds();
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
    @DisplayName("findAll - Success")
    void test_FindAll_Success() throws Exception {
        // Given
        given(this.adService.findAll()).willReturn(this.ads);

        this.mockMvc.perform(get(this.baseUrlAd + "/findAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Ads Success"))
                .andExpect(jsonPath("$.data[0].id").value(this.ads.get(0).getId()))
                .andExpect(jsonPath("$.data[0].createDate").value(this.ads.get(0).getCreateDate().toString()))
                .andExpect(jsonPath("$.data[0].htmlCode").value(this.ads.get(0).getHtmlCode()))
                .andExpect(jsonPath("$.data[0].job.id").value(this.ads.get(0).getJob().getId()))
                .andExpect(jsonPath("$.data[0].job.title").value(this.ads.get(0).getJob().getTitle()))
                .andExpect(jsonPath("$.data[0].job.description").value(this.ads.get(0).getJob().getDescription()))
                .andExpect(jsonPath("$.data[0].job.instruction").value(this.ads.get(0).getJob().getInstruction()))
                .andExpect(jsonPath("$.data[0].job.recruiterName").value(this.ads.get(0).getJob().getRecruiterName()))
                .andExpect(jsonPath("$.data[0].job.adCompany").value(this.ads.get(0).getJob().getAdCompany()))
                .andExpect(jsonPath("$.data[0].job.adEmail").value(this.ads.get(0).getJob().getAdEmail()))
                .andExpect(jsonPath("$.data[0].job.adPhone").value(this.ads.get(0).getJob().getAdPhone()))
                .andExpect(jsonPath("$.data[0].job.applicationDeadline").value(this.ads.get(0).getJob().getApplicationDeadline()))
                .andExpect(jsonPath("$.data[1].id").value(this.ads.get(1).getId()))
                .andExpect(jsonPath("$.data[1].createDate").value(this.ads.get(1).getCreateDate().toString()))
                .andExpect(jsonPath("$.data[1].htmlCode").value(this.ads.get(1).getHtmlCode()))
                .andExpect(jsonPath("$.data[1].job.id").value(this.ads.get(1).getJob().getId()))
                .andExpect(jsonPath("$.data[1].job.title").value(this.ads.get(1).getJob().getTitle()))
                .andExpect(jsonPath("$.data[1].job.description").value(this.ads.get(1).getJob().getDescription()))
                .andExpect(jsonPath("$.data[1].job.instruction").value(this.ads.get(1).getJob().getInstruction()))
                .andExpect(jsonPath("$.data[1].job.recruiterName").value(this.ads.get(1).getJob().getRecruiterName()))
                .andExpect(jsonPath("$.data[1].job.adCompany").value(this.ads.get(1).getJob().getAdCompany()))
                .andExpect(jsonPath("$.data[1].job.adEmail").value(this.ads.get(1).getJob().getAdEmail()))
                .andExpect(jsonPath("$.data[1].job.adPhone").value(this.ads.get(1).getJob().getAdPhone()))
                .andExpect(jsonPath("$.data[1].job.applicationDeadline").value(this.ads.get(1).getJob().getApplicationDeadline()))
                .andExpect(jsonPath("$.data[2].id").value(this.ads.get(2).getId()))
                .andExpect(jsonPath("$.data[2].createDate").value(this.ads.get(2).getCreateDate().toString()))
                .andExpect(jsonPath("$.data[2].htmlCode").value(this.ads.get(2).getHtmlCode()))
                .andExpect(jsonPath("$.data[2].job.id").value(this.ads.get(2).getJob().getId()))
                .andExpect(jsonPath("$.data[2].job.title").value(this.ads.get(2).getJob().getTitle()))
                .andExpect(jsonPath("$.data[2].job.description").value(this.ads.get(2).getJob().getDescription()))
                .andExpect(jsonPath("$.data[2].job.instruction").value(this.ads.get(2).getJob().getInstruction()))
                .andExpect(jsonPath("$.data[2].job.recruiterName").value(this.ads.get(2).getJob().getRecruiterName()))
                .andExpect(jsonPath("$.data[2].job.adCompany").value(this.ads.get(2).getJob().getAdCompany()))
                .andExpect(jsonPath("$.data[2].job.adEmail").value(this.ads.get(2).getJob().getAdEmail()))
                .andExpect(jsonPath("$.data[2].job.adPhone").value(this.ads.get(2).getJob().getAdPhone()))
                .andExpect(jsonPath("$.data[2].job.applicationDeadline").value(this.ads.get(2).getJob().getApplicationDeadline()));
    }
}