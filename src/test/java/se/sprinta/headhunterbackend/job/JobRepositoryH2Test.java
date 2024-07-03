package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test-h2")
class JobRepositoryH2Test {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private H2DatabaseInitializer dbInit;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE job ALTER COLUMN id RESTART WITH 1");
        this.dbInit.initializeH2Database();
    }

    @AfterEach
    void tearDown() {
        this.dbInit.clearDatabase();
    }

    @Test
    @DisplayName("getAllJobDtosByUserEmail - Success")
    void test_GetAllJobDtosByUserEmail_Success() {
        List<JobDtoView> foundJobDtoViews = this.jobRepository.getAllJobDtosByUserEmail("user1-h2@hh.se");

        System.out.println("\ngetAllJobDtosByUserEmail (user1-h2@hh.se)");
        System.out.println("size: " + foundJobDtoViews.size());
        for (JobDtoView job : foundJobDtoViews) {
            System.out.println(job.title());
        }

        assertEquals(foundJobDtoViews.size(), 2);
        assertEquals(foundJobDtoViews.get(0).title(), "job1 Title 1");
        assertEquals(foundJobDtoViews.get(0).description(), "job1 Description 1");
        assertNull(foundJobDtoViews.get(0).recruiterName());
        assertNull(foundJobDtoViews.get(0).adCompany());
        assertNull(foundJobDtoViews.get(0).adEmail());
        assertNull(foundJobDtoViews.get(0).adPhone());
        assertEquals(foundJobDtoViews.get(0).applicationDeadline(), "job1 applicationDeadline 1");
        assertEquals(foundJobDtoViews.get(1).title(), "job2 Title 2");
        assertEquals(foundJobDtoViews.get(1).description(), "job2 Description 2");
        assertNull(foundJobDtoViews.get(1).recruiterName());
        assertNull(foundJobDtoViews.get(1).adCompany());
        assertNull(foundJobDtoViews.get(1).adEmail());
        assertNull(foundJobDtoViews.get(1).adPhone());
        assertEquals(foundJobDtoViews.get(1).applicationDeadline(), "job2 applicationDeadline 2");
    }

    @Test
    @DisplayName("getAllJobDtos - Success")
    void test_getAllJobDtos_Success() {
        List<JobDtoView> foundJobDtoViews = this.jobRepository.getAllJobDtos();

        System.out.println("\ngetAllJobDtos");
        System.out.println("size: " + foundJobDtoViews.size());

        for (JobDtoView job : foundJobDtoViews) {
            System.out.println(job.title());
        }

        assertEquals(foundJobDtoViews.size(), 4);

        assertEquals(foundJobDtoViews.get(0).title(), "job1 Title 1");
        assertEquals(foundJobDtoViews.get(0).description(), "job1 Description 1");
        assertNull(foundJobDtoViews.get(0).recruiterName());
        assertNull(foundJobDtoViews.get(0).adCompany());
        assertNull(foundJobDtoViews.get(0).adEmail());
        assertNull(foundJobDtoViews.get(0).adPhone());
        assertEquals(foundJobDtoViews.get(0).applicationDeadline(), "job1 applicationDeadline 1");
        assertEquals(foundJobDtoViews.get(1).title(), "job2 Title 2");
        assertEquals(foundJobDtoViews.get(1).description(), "job2 Description 2");
        assertNull(foundJobDtoViews.get(1).recruiterName());
        assertNull(foundJobDtoViews.get(1).adCompany());
        assertNull(foundJobDtoViews.get(1).adEmail());
        assertNull(foundJobDtoViews.get(1).adPhone());
        assertEquals(foundJobDtoViews.get(1).applicationDeadline(), "job2 applicationDeadline 2");
        assertEquals(foundJobDtoViews.get(2).title(), "job3 Title 3");
        assertEquals(foundJobDtoViews.get(2).description(), "job3 Description 3");
        assertNull(foundJobDtoViews.get(2).recruiterName());
        assertNull(foundJobDtoViews.get(2).adCompany());
        assertNull(foundJobDtoViews.get(2).adEmail());
        assertNull(foundJobDtoViews.get(2).adPhone());
        assertEquals(foundJobDtoViews.get(2).applicationDeadline(), "job3 applicationDeadline 3");
    }

    @Test
    @DisplayName("getFullJobDtoByJobId - Success")
    void test_GetFullJobDtoByJobId_Success() {
        Optional<JobDtoView> foundJobDtoView = this.jobRepository.getFullJobDtoByJobId(1L);

        if (foundJobDtoView.isPresent()) {
            assertEquals(foundJobDtoView.get().title(), "job1 Title 1");
            assertEquals(foundJobDtoView.get().description(), "job1 Description 1");
            assertNull(foundJobDtoView.get().recruiterName());
            assertNull(foundJobDtoView.get().adCompany());
            assertNull(foundJobDtoView.get().adEmail());
            assertNull(foundJobDtoView.get().adPhone());
            assertEquals(foundJobDtoView.get().applicationDeadline(), "job1 applicationDeadline 1");

        }
    }

    @Test
    @DisplayName("getAllJobCardsByUserEmail - Success")
    void test_GetAllJobCardsByUserEmail_Success() {
        List<JobCardDtoView> foundJobCardDtoView = this.jobRepository.getAllJobCardsByUserEmail("user1-h2@hh.se");

        System.out.println("\ngetAllJobCardsByUserEmail (user1-h2@hh.se)");
        System.out.println("size: " + foundJobCardDtoView.size());
        for (JobCardDtoView job : foundJobCardDtoView) {
            System.out.println(job.title());
        }

        assertEquals(foundJobCardDtoView.size(), 2);

        assertEquals(foundJobCardDtoView.get(0).id(), 1L);
        assertEquals(foundJobCardDtoView.get(0).title(), "job1 Title 1");
        assertEquals(foundJobCardDtoView.get(0).applicationDeadline(), "job1 applicationDeadline 1");
        assertEquals(foundJobCardDtoView.get(1).id(), 2L);
        assertEquals(foundJobCardDtoView.get(1).title(), "job2 Title 2");
        assertEquals(foundJobCardDtoView.get(1).applicationDeadline(), "job2 applicationDeadline 2");
    }
}