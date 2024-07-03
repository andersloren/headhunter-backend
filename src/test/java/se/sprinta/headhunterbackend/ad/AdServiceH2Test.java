package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test-h2")
public class AdServiceH2Test {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AdService adService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private H2DatabaseInitializer h2DbInit;

    List<Job> jobs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE job ALTER COLUMN id RESTART WITH 1");
        this.jobs = this.h2DbInit.getJobs();
        this.h2DbInit.initializeH2Database();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearDatabase();
    }

    @Test
    @DisplayName("findAll - Success")
    void test_FindAll_Success() {
        List<Ad> allAds = this.adService.findAll();

        for (Ad ad : allAds) {
            System.out.println(ad.getId());
            System.out.println(ad.getHtmlCode());
            System.out.println(ad.getJob());
            System.out.println(ad.getCreatedDateTime());
        }

        assertEquals(allAds.size(), 3);

        assertEquals(allAds.get(0).getHtmlCode(), "htmlCode 1");
        assertTrue(allAds.get(0).getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(allAds.get(0).getId());
        assertEquals(allAds.get(0).getJob().getTitle(), "job1 Title 1");
        assertEquals(allAds.get(0).getJob().getDescription(), "job1 Description 1");
        assertEquals(allAds.get(0).getJob().getInstruction(), "job1 Instruction 1");
        assertNull(allAds.get(0).getJob().getRecruiterName());
        assertNull(allAds.get(0).getJob().getAdCompany());
        assertNull(allAds.get(0).getJob().getAdEmail());
        assertNull(allAds.get(0).getJob().getAdPhone());
        assertEquals(allAds.get(0).getJob().getApplicationDeadline(), "job1 applicationDeadline 1");
        assertEquals(allAds.get(1).getHtmlCode(), "htmlCode 2");
        assertTrue(allAds.get(1).getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(allAds.get(1).getId());
        assertEquals(allAds.get(1).getJob().getTitle(), "job1 Title 1");
        assertEquals(allAds.get(1).getJob().getDescription(), "job1 Description 1");
        assertEquals(allAds.get(1).getJob().getInstruction(), "job1 Instruction 1");
        assertNull(allAds.get(1).getJob().getRecruiterName());
        assertNull(allAds.get(1).getJob().getAdCompany());
        assertNull(allAds.get(1).getJob().getAdEmail());
        assertNull(allAds.get(1).getJob().getAdPhone());
        assertEquals(allAds.get(1).getJob().getApplicationDeadline(), "job1 applicationDeadline 1");
        assertEquals(allAds.get(2).getHtmlCode(), "htmlCode 3");
        assertTrue(allAds.get(2).getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(allAds.get(2).getId());
        assertEquals(allAds.get(2).getJob().getTitle(), "job2 Title 2");
        assertEquals(allAds.get(2).getJob().getDescription(), "job2 Description 2");
        assertEquals(allAds.get(2).getJob().getInstruction(), "job2 Instruction 2");
        assertNull(allAds.get(2).getJob().getRecruiterName());
        assertNull(allAds.get(2).getJob().getAdCompany());
        assertNull(allAds.get(2).getJob().getAdEmail());
        assertNull(allAds.get(2).getJob().getAdPhone());
        assertEquals(allAds.get(2).getJob().getApplicationDeadline(), "job2 applicationDeadline 2");
    }

    @Test
    @DisplayName("getAllAdDtos - Success")
    void test_GetAllAdDtos_Success() {
        List<AdDtoView> foundAdDtos = this.adService.getAllAdDtos();

        for (AdDtoView adDti : foundAdDtos) {
            System.out.println(adDti.id());
            System.out.println(adDti.htmlCode());
            System.out.println(adDti.createdDateTime());
        }

        assertNotNull(foundAdDtos.get(0).id());
        assertEquals(foundAdDtos.get(0).htmlCode(), "htmlCode 1");
        assertTrue(foundAdDtos.get(0).createdDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(foundAdDtos.get(1).id());
        assertEquals(foundAdDtos.get(1).htmlCode(), "htmlCode 2");
        assertTrue(foundAdDtos.get(1).createdDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(foundAdDtos.get(2).id());
        assertEquals(foundAdDtos.get(2).htmlCode(), "htmlCode 3");
        assertTrue(foundAdDtos.get(2).createdDateTime().isBefore(ZonedDateTime.now()));
    }

    @Test
    @DisplayName("findById - Success")
    void test_FindById_Success() {
        String adId = this.adService.findAll().get(0).getId();

        Ad foundAd = this.adService.findById(adId);

        System.out.println(foundAd.getId());
        System.out.println(foundAd.getHtmlCode());
        System.out.println(foundAd.getCreatedDateTime());

        assertEquals(foundAd.getHtmlCode(), "htmlCode 1");
        assertTrue(foundAd.getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(foundAd.getId());
        assertEquals(foundAd.getJob().getTitle(), "job1 Title 1");
        assertEquals(foundAd.getJob().getDescription(), "job1 Description 1");
        assertEquals(foundAd.getJob().getInstruction(), "job1 Instruction 1");
        assertNull(foundAd.getJob().getRecruiterName());
        assertNull(foundAd.getJob().getAdCompany());
        assertNull(foundAd.getJob().getAdEmail());
        assertNull(foundAd.getJob().getAdPhone());
        assertEquals(foundAd.getJob().getApplicationDeadline(), "job1 applicationDeadline 1");
    }

    @Test
    @DisplayName("findById - Invalid Ad Id - Exception")
    void test_FindById_InvalidAdId_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.findById("Invalid Id"));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id Invalid Id");
    }

    @Test
    @DisplayName("getAdsByJobId - Success")
    void test_GetAdsByJobId_Success() {
        List<Ad> foundAds = this.adService.getAdsByJobId(1L);

        for (Ad ad : foundAds) {
            System.out.println(ad.getId());
            System.out.println(ad.getHtmlCode());
            System.out.println(ad.getJob());
            System.out.println(ad.getCreatedDateTime());
        }

        assertEquals(foundAds.get(0).getHtmlCode(), "htmlCode 1");
        assertTrue(foundAds.get(0).getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(foundAds.get(0).getId());
        assertEquals(foundAds.get(0).getJob().getTitle(), "job1 Title 1");
        assertEquals(foundAds.get(0).getJob().getDescription(), "job1 Description 1");
        assertEquals(foundAds.get(0).getJob().getInstruction(), "job1 Instruction 1");
        assertNull(foundAds.get(0).getJob().getRecruiterName());
        assertNull(foundAds.get(0).getJob().getAdCompany());
        assertNull(foundAds.get(0).getJob().getAdEmail());
        assertNull(foundAds.get(0).getJob().getAdPhone());
        assertEquals(foundAds.get(0).getJob().getApplicationDeadline(), "job1 applicationDeadline 1");
        assertEquals(foundAds.get(1).getHtmlCode(), "htmlCode 2");
        assertTrue(foundAds.get(1).getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(foundAds.get(1).getId());
        assertEquals(foundAds.get(1).getJob().getTitle(), "job1 Title 1");
        assertEquals(foundAds.get(1).getJob().getDescription(), "job1 Description 1");
        assertEquals(foundAds.get(1).getJob().getInstruction(), "job1 Instruction 1");
        assertNull(foundAds.get(1).getJob().getRecruiterName());
        assertNull(foundAds.get(1).getJob().getAdCompany());
        assertNull(foundAds.get(1).getJob().getAdEmail());
        assertNull(foundAds.get(1).getJob().getAdPhone());
        assertEquals(foundAds.get(1).getJob().getApplicationDeadline(), "job1 applicationDeadline 1");
    }

    @Test
    @DisplayName("getAdsByJobId - Invalid Job Id - Exception")
    void test_GetAdsByJobId_InvalidJobId_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdsByJobId(Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("getAdDtosByJobId - Success")
    void test_GetAdDtosByJobId_Success() {
        List<AdDtoView> foundAdDtos = this.adService.getAdDtosByJobId(1L);

        for (AdDtoView adDti : foundAdDtos) {
            System.out.println(adDti.id());
            System.out.println(adDti.htmlCode());
            System.out.println(adDti.createdDateTime());
        }

        assertNotNull(foundAdDtos.get(0).id());
        assertEquals(foundAdDtos.get(0).htmlCode(), "htmlCode 1");
        assertTrue(foundAdDtos.get(0).createdDateTime().isBefore(ZonedDateTime.now()));
        assertNotNull(foundAdDtos.get(1).id());
        assertEquals(foundAdDtos.get(1).htmlCode(), "htmlCode 2");
        assertTrue(foundAdDtos.get(1).createdDateTime().isBefore(ZonedDateTime.now()));
    }

    @Test
    @DisplayName("getAdDtosByJobId - Invalid Job Id - Exception")
    void test_GetAdDtosByJobId_InvalidJobId_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdDtosByJobId(Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }
}

