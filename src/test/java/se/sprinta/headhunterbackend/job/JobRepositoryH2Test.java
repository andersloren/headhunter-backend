package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.job.dto.JobIdAndTitleDtoView;

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
    private H2DatabaseInitializer dbInit;

    @BeforeEach
    void setUp() {
        this.dbInit.initializeDatabase();
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
        assertNull(foundJobDtoViews.get(0).applicationDeadline());
        assertEquals(foundJobDtoViews.get(1).title(), "job2 Title 2");
        assertEquals(foundJobDtoViews.get(1).description(), "job2 Description 2");
        assertNull(foundJobDtoViews.get(1).recruiterName());
        assertNull(foundJobDtoViews.get(1).adCompany());
        assertNull(foundJobDtoViews.get(1).adEmail());
        assertNull(foundJobDtoViews.get(1).adPhone());
        assertNull(foundJobDtoViews.get(1).applicationDeadline());
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
        assertNull(foundJobDtoViews.get(0).applicationDeadline());
        assertEquals(foundJobDtoViews.get(1).title(), "job2 Title 2");
        assertEquals(foundJobDtoViews.get(1).description(), "job2 Description 2");
        assertNull(foundJobDtoViews.get(1).recruiterName());
        assertNull(foundJobDtoViews.get(1).adCompany());
        assertNull(foundJobDtoViews.get(1).adEmail());
        assertNull(foundJobDtoViews.get(1).adPhone());
        assertNull(foundJobDtoViews.get(1).applicationDeadline());
        assertEquals(foundJobDtoViews.get(2).title(), "job3 Title 3");
        assertEquals(foundJobDtoViews.get(2).description(), "job3 Description 3");
        assertNull(foundJobDtoViews.get(2).recruiterName());
        assertNull(foundJobDtoViews.get(2).adCompany());
        assertNull(foundJobDtoViews.get(2).adEmail());
        assertNull(foundJobDtoViews.get(2).adPhone());
        assertNull(foundJobDtoViews.get(2).applicationDeadline());
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
            assertNull(foundJobDtoView.get().applicationDeadline());
        }
    }

    @Test
    @DisplayName("getAllJobsDtoIdAndTitlesByEmail - Success")
    void test_GetAllJobsDtoIdAndTitlesByEmail_Success() {
        List<JobIdAndTitleDtoView> foundJobIdAndTitleDtoViews = this.jobRepository.getAllJobsDtoIdAndTitlesByEmail("user1-h2@hh.se");

        System.out.println("\ngetAllJobsDtoIdAndTitlesByEmail (user1-h2@hh.se)");
        System.out.println("size: " + foundJobIdAndTitleDtoViews.size());
        for (JobIdAndTitleDtoView job : foundJobIdAndTitleDtoViews) {
            System.out.println(job.title());
        }

        assertEquals(foundJobIdAndTitleDtoViews.size(), 2);

        assertEquals(foundJobIdAndTitleDtoViews.get(0).title(), "job1 Title 1");
        assertEquals(foundJobIdAndTitleDtoViews.get(0).id(), 1L);
        assertEquals(foundJobIdAndTitleDtoViews.get(1).title(), "job2 Title 2");
        assertEquals(foundJobIdAndTitleDtoViews.get(1).id(), 2L);
    }
}