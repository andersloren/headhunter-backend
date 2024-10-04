package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.TestsDatabaseInitializer;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AdServiceTest {

    @Autowired
    private AdService adService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestsDatabaseInitializer h2DbInit;

    List<Job> jobs = new ArrayList<>();

    List<Ad> ads = new ArrayList<>();

    List<AdDtoView> adDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE job ALTER COLUMN id RESTART WITH 1");
        this.h2DbInit.initializeH2Database();
        this.jobs = TestsDatabaseInitializer.getJobs();
        this.ads = TestsDatabaseInitializer.getAds();
        this.adDtos = this.h2DbInit.initializeH2AdDtos();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearH2Database();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_databaseInitializer() {
        assertEquals(this.jobs.size(), 4);
        assertEquals(this.ads.size(), 3);
        assertEquals(this.adDtos.size(), 3);
    }

    @Test
    @DisplayName("findAll - Success")
    void test_FindAll_Success() {
        // When
        List<Ad> allAds = this.adService.findAll();

        // Then
        assertEquals(allAds.size(), 3);
        for (int i = 0; i < allAds.size(); i++) {
            assertEquals(allAds.get(i).getHtmlCode(), this.ads.get(i).getHtmlCode());
            assertFalse(allAds.get(i).getDateCreated().isAfter(LocalDate.now()));
            assertNotNull(allAds.get(i).getId(), this.ads.get(i).getId());
            assertEquals(allAds.get(i).getJob().getTitle(), this.ads.get(i).getJob().getTitle());
            assertEquals(allAds.get(i).getJob().getDescription(), this.ads.get(i).getJob().getDescription());
            assertEquals(allAds.get(i).getJob().getInstruction(), this.ads.get(i).getJob().getInstruction());
            assertEquals(allAds.get(i).getJob().getRecruiterName(), this.ads.get(i).getJob().getRecruiterName());
            assertEquals(allAds.get(i).getJob().getAdCompany(), this.ads.get(i).getJob().getAdCompany());
            assertEquals(allAds.get(i).getJob().getAdEmail(), this.ads.get(i).getJob().getAdEmail());
            assertEquals(allAds.get(i).getJob().getAdPhone(), this.ads.get(i).getJob().getAdPhone());
            assertEquals(allAds.get(i).getJob().getApplicationDeadline(), this.ads.get(i).getJob().getApplicationDeadline());

        }
    }

    @Test
    @DisplayName("getAllAdDtos - Success")
    void test_GetAdDtos_Success() {
        // When
        List<AdDtoView> foundAdDtos = this.adService.getAdDtos();

        // Then

        for (int i = 0; i < foundAdDtos.size(); i++) {
            assertEquals(foundAdDtos.get(i).id(), this.adDtos.get(i).id());
            assertEquals(foundAdDtos.get(i).htmlCode(), this.adDtos.get(i).htmlCode());
            assertFalse(foundAdDtos.get(i).dateCreated().isAfter(LocalDate.now()));
        }
    }

    @Test
    @DisplayName("findById - Success")
    void test_FindById_Success() {
        String adId = this.adService.findAll().get(0).getId();

        // When
        Ad foundAd = this.adService.findById(adId);

        // Then
        assertEquals(foundAd.getHtmlCode(), this.ads.get(0).getHtmlCode());
        assertFalse(foundAd.getDateCreated().isAfter(LocalDate.now()));
        assertNotNull(foundAd.getId(), this.ads.get(0).getId());
        assertEquals(foundAd.getJob().getTitle(), this.ads.get(0).getJob().getTitle());
        assertEquals(foundAd.getJob().getDescription(), this.ads.get(0).getJob().getDescription());
        assertEquals(foundAd.getJob().getInstruction(), this.ads.get(0).getJob().getInstruction());
        assertEquals(foundAd.getJob().getRecruiterName(), this.ads.get(0).getJob().getRecruiterName());
        assertEquals(foundAd.getJob().getAdCompany(), this.ads.get(0).getJob().getAdCompany());
        assertEquals(foundAd.getJob().getAdEmail(), this.ads.get(0).getJob().getAdEmail());
        assertEquals(foundAd.getJob().getApplicationDeadline(), this.ads.get(0).getJob().getApplicationDeadline());
        assertEquals(foundAd.getJob().getAdPhone(), this.ads.get(0).getJob().getAdPhone());
    }

    @Test
    @DisplayName("GET - findById - Invalid Ad Id - Exception")
    void test_FindById_InvalidAdId_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.findById("Invalid Id"));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id Invalid Id");
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - Success")
    void test_GetAdsByJobId_Success() {
        // When
        List<AdDtoView> foundAdDtos = this.adService.getAdDtosByJobId(1L);

        // Then
        for (int i = 0; i < foundAdDtos.size(); i++) {
            assertNotNull(foundAdDtos.get(i).id());
            assertEquals(foundAdDtos.get(i).htmlCode(), this.adDtos.get(i).htmlCode());
            assertFalse(foundAdDtos.get(i).dateCreated().isAfter(LocalDate.now()));
        }
    }

    @Test
    @DisplayName("GET - getAdsByJobId - Invalid Job Id - Exception")
    void test_GetAdsByJobId_InvalidJobId_Exception() {
        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdDtosByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("GET - numberOfAdsByJobId - Success")
    void test_getNumberOfAdsByJobId() {
        // When
        long numberOfJobs = this.adService.getNumberOfAdsByJobId(1L);

        // Then
        assertEquals(numberOfJobs, 2);
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - Invalid Job Id - Success")
    void test_GetNumberOfJobsById_InvalidJobId_Exception() {
        // Wen
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getNumberOfAdsByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("POST - addAd - Success")
    void test_AddAd_Success() {
        AdDtoForm newAdDtoForm = new AdDtoForm("HtmlCode");
        // When
        Ad newAd = this.adService.addAd(this.jobs.get(0).getId(), newAdDtoForm);

        // Then
        assertEquals(newAd.getHtmlCode(), newAdDtoForm.htmlCode());
    }

    @Test
    @DisplayName("POST - addAd - Invalid Job Id - Exception")
    void test_AddAd_InvalidJobId_Exception() {
        AdDtoForm newAdDtoForm = new AdDtoForm("HtmlCode");
        // Wen
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.addAd(Long.MAX_VALUE, newAdDtoForm));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("DELETE - delete - Success")
    void test_Delete_Success() {
        // When
        this.adService.delete(this.ads.get(0).getId());

        // Then
        assertThrows(ObjectNotFoundException.class,
                () -> this.adService.findById(this.ads.get(0).getId()));

        assertEquals(this.adService.findAll().size(), this.ads.size() - 1);
    }
}
