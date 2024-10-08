package se.sprinta.headhunterbackend.job;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.TestsDatabaseInitializer;
import se.sprinta.headhunterbackend.account.AccountRepository;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class JobServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestsDatabaseInitializer h2DbInit;

    private List<Job> jobs = new ArrayList<>();

    private List<JobDtoView> jobDtos = new ArrayList<>();

    private List<JobCardDtoView> jobCardDtoViews = new ArrayList<>();

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE job ALTER COLUMN id RESTART WITH 1");
        this.h2DbInit.initializeH2Database();
        this.jobs = TestsDatabaseInitializer.getJobs();
        this.jobDtos = this.h2DbInit.initializeH2JobDtos();
        this.jobCardDtoViews = this.h2DbInit.initializeH2JobCardDtos();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearH2Database();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_databaseInitializer() {
        assertEquals(this.jobs.size(), 4);
        assertEquals(this.jobDtos.size(), 4);
        assertEquals(this.jobCardDtoViews.size(), 4);
    }

    @Test
    @DisplayName("GET - findAll - Success")
    void test_FindSuccess_Success() {
        List<Job> allJobs = this.jobService.findAll();

        assertEquals(allJobs.size(), 4);

        for (int i = 0; i < allJobs.size(); i++) {
            if (!allJobs.get(i).equals(this.jobs.get(i))) {
                assertEquals(allJobs.get(i).getId(), i + 1);
                assertEquals(allJobs.get(i).getTitle(), this.jobs.get(i).getTitle());
                assertEquals(allJobs.get(i).getDescription(), this.jobs.get(i).getDescription());
                assertEquals(allJobs.get(i).getInstruction(), this.jobs.get(i).getInstruction());
                assertEquals(allJobs.get(i).getRecruiterName(), "");
                assertEquals(allJobs.get(i).getAdCompany(), "");
                assertEquals(allJobs.get(i).getAdEmail(), "");
                assertEquals(allJobs.get(i).getAdPhone(), "");
                assertEquals(allJobs.get(i).getApplicationDeadline(), this.jobs.get(i).getApplicationDeadline());
            }
        }
    }

    @Test
    @DisplayName("GET - getJobDtos - Success")
    void test_GetJobDtos_Success() {
        List<JobDtoView> allJobDtos = this.jobService.getJobDtos();

        assertEquals(allJobDtos.size(), 4);

        for (int i = 0; i < allJobDtos.size(); i++) {
            assertEquals(allJobDtos.get(i).title(), this.jobDtos.get(i).title());
            assertEquals(allJobDtos.get(i).description(), this.jobDtos.get(i).description());
            assertEquals(allJobDtos.get(i).recruiterName(), this.jobDtos.get(i).recruiterName());
            assertEquals(allJobDtos.get(i).adCompany(), this.jobDtos.get(i).adCompany());
            assertEquals(allJobDtos.get(i).adEmail(), this.jobDtos.get(i).adEmail());
            assertEquals(allJobDtos.get(i).adPhone(), this.jobDtos.get(i).adPhone());
            assertEquals(allJobDtos.get(i).applicationDeadline(), this.jobDtos.get(i).applicationDeadline());
        }
    }

    @Test
    @DisplayName("GET - getJobDtosByEmail - Success")
    void test_GetJobsByEmail_Success() {
        String email = "user1-test@hh.se";
        List<JobDtoView> allJobDtos = this.jobService.getJobDtosByEmail(email);

        for (int i = 0; i < allJobDtos.size(); i++) {
            assertEquals(allJobDtos.get(i).title(), this.jobDtos.get(i).title());
            assertEquals(allJobDtos.get(i).description(), this.jobDtos.get(i).description());
            assertEquals(allJobDtos.get(i).recruiterName(), this.jobDtos.get(i).recruiterName());
            assertEquals(allJobDtos.get(i).adCompany(), this.jobDtos.get(i).adCompany());
            assertEquals(allJobDtos.get(i).adEmail(), this.jobDtos.get(i).adEmail());
            assertEquals(allJobDtos.get(i).adPhone(), this.jobDtos.get(i).adPhone());
            assertEquals(allJobDtos.get(i).applicationDeadline(), this.jobDtos.get(i).applicationDeadline());
        }
    }

    @Test
    @DisplayName("GET - getJobDtosByEmail - Invalid Email - Exception")
    void test_GetJobDtosByEmail_InvalidEmail_Exception() {
        // Given
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.getJobDtosByEmail("abc"));

        // When and then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");
    }

    @Test
    @DisplayName("GET - getJobCardsByEmail - Success")
    void test_GetJobCardsByEmail_Success() {
        String email = "user1-test@hh.se";
        List<JobCardDtoView> allJobCards = this.jobService.getJobCardDtosByEmail(email);

        assertEquals(2, allJobCards.size());

        for (int i = 0; i < allJobCards.size(); i++) {
            assertEquals(allJobCards.get(i).title(), this.jobCardDtoViews.get(i).title());
            assertEquals(allJobCards.get(i).applicationDeadline(), this.jobCardDtoViews.get(i).applicationDeadline());
        }
    }

    @Test
    @DisplayName("GET - getJobCardsByEmail - Invalid Email - Exception")
    void test_GetJobCardsByEmail_InvalidEmail_Exception() {
        // Given
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.getJobCardDtosByEmail("abc"));

        // When and then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");
    }

    @Test
    @DisplayName("GET - findById - Success")
    void test_FindById_Success() {
        long jobId = 1L;

        Job foundJob = this.jobService.findById(jobId);

        assertEquals(foundJob.getId(), 1L);

        if (!foundJob.equals(this.jobs.get(0))) {
            assertEquals(foundJob.getId(), 1L);
            assertEquals(foundJob.getTitle(), this.jobs.get(0).getTitle());
            assertEquals(foundJob.getDescription(), this.jobs.get(0).getDescription());
            assertEquals(foundJob.getInstruction(), this.jobs.get(0).getInstruction());
            assertEquals(foundJob.getRecruiterName(), "");
            assertEquals(foundJob.getAdCompany(), "");
            assertEquals(foundJob.getAdEmail(), "");
            assertEquals(foundJob.getAdPhone(), "");
            assertEquals(foundJob.getApplicationDeadline(), this.jobs.get(0).getApplicationDeadline());
        }
    }

    @Test
    @DisplayName("GET - findById - Invalid Id - Exception")
    void test_FindById_InvalidId_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.findById(Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("GET - getJobDto - Success")
    void test_GetJobDtoByJobId_Success() {
        long jobId = 1L;
        JobDtoView jobDtoView = this.jobService.getJobDto(jobId);

        if (!jobDtoView.equals(this.jobDtos.get(0))) {
            assertEquals(jobDtoView.title(), this.jobDtos.get(0).title());
            assertEquals(jobDtoView.description(), this.jobDtos.get(0).description());
            assertNull(jobDtoView.recruiterName());
            assertNull(jobDtoView.adCompany());
            assertNull(jobDtoView.adEmail());
            assertNull(jobDtoView.adPhone());
            assertEquals(jobDtoView.applicationDeadline(), this.jobDtos.get(0).applicationDeadline());
        }
    }

    @Test
    @DisplayName("GET - getJobDtoByJobId - Invalid Job Id - Exception")
    void test_GetJobDto_InvalidId_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.getJobDto(Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("POST - addJob - Success")
    void test_AddJob_Success() {
        List<Job> all = this.jobService.findAll();
        for (Job job : all) {
            System.out.println(job.getId());
        }

        String email = "user1-test@hh.se";
        Job newJob = new Job(
                "job5 Title 5",
                "job5 Description 5",
                "job5 Instruction 5");

        Job addedJob = this.jobService.addJob(email, newJob);

        assertEquals(addedJob.getId(), 5L);
        assertEquals(addedJob.getTitle(), "job5 Title 5");
        assertEquals(addedJob.getDescription(), "job5 Description 5");
        assertEquals(addedJob.getInstruction(), "job5 Instruction 5");
        assertEquals(addedJob.getRecruiterName(), "");
        assertEquals(addedJob.getAdCompany(), "");
        assertEquals(addedJob.getAdEmail(), "");
        assertEquals(addedJob.getAdPhone(), "");

        Optional<AccountDtoView> foundAccountDto = this.accountRepository.getAccountDtoByEmail(email);

        if (foundAccountDto.isPresent()) {

            assertEquals(foundAccountDto.get().number_of_jobs(), 3);
            assertEquals(foundAccountDto.get().email(), "user1-test@hh.se");
            assertEquals(foundAccountDto.get().roles(), "user");
        }
    }

    @Test
    @DisplayName("POST - addJob - Invalid Input - Exception")
    void test_AddJob_InvalidInput_Exception() {
        Job newJob = new Job(
                "job5 Title 5",
                "job5 Description 5",
                "job5 Instruction 5");

        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.addJob("Invalid Email", newJob));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("PUT - update - Success")
    void test_Update_Success() {
        long jobId = 1L;
        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "updated title",
                "updated description",
                "updated instruction",
                "updated recruiterName",
                "updated adCompany",
                "updated adEmail",
                "updated adPhone",
                "updated applicationDeadline");

        Job update = this.jobService.update(jobId, jobDtoFormUpdate);

        assertEquals(jobDtoFormUpdate.title(), update.getTitle());
        assertEquals(jobDtoFormUpdate.description(), update.getDescription());
        assertEquals(jobDtoFormUpdate.instruction(), update.getInstruction());
        assertEquals(jobDtoFormUpdate.recruiterName(), update.getRecruiterName());
        assertEquals(jobDtoFormUpdate.adCompany(), update.getAdCompany());
        assertEquals(jobDtoFormUpdate.adEmail(), update.getAdEmail());
        assertEquals(jobDtoFormUpdate.adPhone(), update.getAdPhone());
        assertEquals(jobDtoFormUpdate.applicationDeadline(), update.getApplicationDeadline());
    }

    @Test
    @DisplayName("PUT - update - Null Form - Exception")
    void testUpdateNullJobDtoFormUpdate() {
        long jobId = 1L;

        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.jobService.update(jobId, null));

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Update can't be null");
    }

    @Test
    @DisplayName("PUT - update - Invalid Input - Exception")
    void test_Update_InvalidInput_Exception() {
        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "updated title",
                "updated description",
                "updated instruction",
                "updated recruiterName",
                "updated adCompany",
                "updated adEmail",
                "updated adPhone",
                "updated applicationDeadline");

        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.update(Long.MAX_VALUE, jobDtoFormUpdate));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    // Delete Success test can be found in JobServiceComplementaryH2Test

    @Test
    @DisplayName("DELETE - delete - Invalid Account Id Input - Exception")
    void test_Delete_InvalidAccountIdInput_Success() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.delete("Invalid Email", 1L));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("DELETE - delete - Invalid Job Id Input - Exception")
    void test_Delete_InvalidJobIdInput_Success() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.delete("user1-test@hh.se", Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }
}
