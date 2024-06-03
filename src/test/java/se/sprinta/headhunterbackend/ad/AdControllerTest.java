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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Title");
        job1.setDescription("Description.");
        job1.setUser(user1);
        job1.setInstruction("Instruction");

        user1.addJob(job1);

        Ad ad1 = new Ad("htmlCode 1", job1);
        Ad ad2 = new Ad("htmlCode 2", job1);
        Ad ad3 = new Ad("htmlCode 3", job1);
        Ad ad4 = new Ad("htmlCode 4", job1);
        Ad ad5 = new Ad("htmlCode 5", job1);

        List<Ad> ads = new ArrayList<>();
        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);
        ads.add(ad4);
        ads.add(ad5);

        job1.addAd(ad1);
        job1.addAd(ad2);
        job1.addAd(ad3);
        job1.addAd(ad4);
        job1.addAd(ad5);
    }

    @Test
    void testFindAllAds() throws Exception {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Title");
        job1.setDescription("Description.");
        job1.setUser(user1);
        job1.setInstruction("Instruction");

        user1.addJob(job1);

        Ad ad1 = new Ad("htmlCode 1", job1);
        Ad ad2 = new Ad("htmlCode 2", job1);
        Ad ad3 = new Ad("htmlCode 3", job1);
        Ad ad4 = new Ad("htmlCode 4", job1);
        Ad ad5 = new Ad("htmlCode 5", job1);

        List<Ad> ads = new ArrayList<>();
        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);
        ads.add(ad4);
        ads.add(ad5);

        given(this.adService.findAllAds()).willReturn(ads);

        this.mockMvc.perform(get(this.baseUrlAds + "/findAllAds").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Ads Success"))
                .andExpect(jsonPath("$.data[0].htmlCode").value("htmlCode 1"))
                .andExpect(jsonPath("$.data[1].htmlCode").value("htmlCode 2"))
                .andExpect(jsonPath("$.data[2].htmlCode").value("htmlCode 3"))
                .andExpect(jsonPath("$.data[3].htmlCode").value("htmlCode 4"))
                .andExpect(jsonPath("$.data[4].htmlCode").value("htmlCode 5"));

        ads.stream().map(ad -> "Ad id: " + ad.getId() + ", Ad HtmlCode: " + ad.getHtmlCode()).forEach(System.out::println);
    }

    @Test
    void testFindAdByIdSuccess() throws Exception {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Title");
        job1.setDescription("Description.");
        job1.setUser(user1);
        job1.setInstruction("Instruction");

        user1.addJob(job1);
        Ad ad1 = new Ad("abc", "htmlCode 1", job1);

        given(this.adService.findById(any(String.class))).willReturn(ad1);

        this.mockMvc.perform(get(this.baseUrlAds + "/findById" + "/randomStringThatMocksAdId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Ad Success"))
                .andExpect(jsonPath("$.data.htmlCode").value("htmlCode 1"));
    }

    @Test
    void testFindUserByAdIdSuccess() throws Exception {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Title");
        job1.setDescription("Description.");
        job1.setUser(user1);
        job1.setInstruction("Instruction");

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
                .andExpect(jsonPath("$.data.roles").value("admin user"))
                .andExpect(jsonPath("$.data.numberOfJobs").value(1));
    }
}
