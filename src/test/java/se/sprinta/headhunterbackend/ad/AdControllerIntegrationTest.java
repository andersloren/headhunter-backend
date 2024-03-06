package se.sprinta.headhunterbackend.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
@Transactional
public class AdControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    AdService adService;
    JobService jobService;
    JobRepository jobRepository;
    AdRepository adRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url-ads}")
    String baseUrlAds;


    @BeforeEach
    void setUp() {
        this.jobService = Mockito.mock(JobService.class);
        this.adRepository = Mockito.mock(AdRepository.class);
        this.jobRepository = Mockito.mock(JobRepository.class);
        this.adService = new AdService(this.adRepository, this.jobService, this.jobRepository);
    }

    @Test
    void testSaveAdSuccess() throws Exception {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setUsername("Mikael");
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("job1 Title");
        job1.setDescription("job1 Description.");
        job1.setUser(user1);
        job1.setInstruction("Job Instruction");

        Long jobId = 1L;

        AdDtoForm adDtoForm = new AdDtoForm(
                "htmlCode"
        );

        Ad ad = new Ad("htmlCode", job1);
        List<Ad> ads = new ArrayList<>();
        job1.setAds(ads);

        String json = this.objectMapper.writeValueAsString(adDtoForm);

        // Given
        given(this.jobService.save(job1)).willReturn(job1);
        given(this.jobRepository.findById(jobId)).willReturn(Optional.of(job1));
        given(this.adRepository.save(ad)).willReturn(ad);
        given(this.adService.addAd(jobId, ad)).willReturn(ad);

        // When and then
        this.mockMvc.perform(post(this.baseUrlAds + "/saveAd/" + jobId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Save Ad Success"))
                .andExpect(jsonPath("$.data.htmlCode").value("htmlCode"));
    }
}
