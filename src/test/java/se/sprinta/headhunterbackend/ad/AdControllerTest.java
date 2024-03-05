package se.sprinta.headhunterbackend.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.User;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class AdControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdService adService;

    @Autowired
    ObjectMapper objectMapper;

    List<Ad> ads;

    @Value("${api.endpoint.base-url-ads}")
    String baseUrlAds;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setUsername("Mikael");
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Title");
        job1.setDescription("Description.");
        job1.setUser(user1);
        job1.setInstruction("Instruction");
        job1.setHtmlCode("htmlCode");

        user1.addJob(job1);

        Ad ad1 = new Ad("htmlCode 1", job1);
        Ad ad2 = new Ad("htmlCode 2", job1);
        Ad ad3 = new Ad("htmlCode 3", job1);
        Ad ad4 = new Ad("htmlCode 4", job1);
        Ad ad5 = new Ad("htmlCode 5", job1);

        job1.addAd(ad1);
        job1.addAd(ad2);
        job1.addAd(ad3);
        job1.addAd(ad4);
        job1.addAd(ad5);
    }

    @Test
    void testFindUserByAdIdSuccess() throws Exception {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setUsername("Mikael");
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Title");
        job1.setDescription("Description.");
        job1.setUser(user1);
        job1.setInstruction("Instruction");
        job1.setHtmlCode("htmlCode");

        user1.addJob(job1);

        // Given
        given(this.adService.findUserByAdId("abc")).willReturn(user1);

        // When and then
        this.mockMvc.perform(get(this.baseUrlAds + "/findUserByAdId/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find User By Ad Id Success"))
                .andExpect(jsonPath("$.data.email").value("m@e.se"))
                .andExpect(jsonPath("$.data.username").value("Mikael"))
                .andExpect(jsonPath("$.data.roles").value("admin user"))
                .andExpect(jsonPath("$.data.numberOfJobs").value(1));
    }
}
