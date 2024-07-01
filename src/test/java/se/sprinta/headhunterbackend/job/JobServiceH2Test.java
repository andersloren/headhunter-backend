package se.sprinta.headhunterbackend.job;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.account.AccountRepository;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.job.dto.JobIdAndTitleDtoView;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test-h2")
@Transactional
public class JobServiceH2Test {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private H2DatabaseInitializer dbInit;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE job ALTER COLUMN id RESTART WITH 1");
        this.dbInit.initializeDatabase();
    }

    @AfterEach
    void tearDown() {
        this.dbInit.clearDatabase();
    }

    @Test
    @DisplayName("findAll - Success")
    void test_FindAllSuccess_Success() {
        List<Job> allJobs = this.jobService.findAll();

        assertEquals(4, allJobs.size());

        assertEquals(allJobs.get(0).getId(), 1L);
        assertEquals(allJobs.get(0).getTitle(), "job1 Title 1");
        assertEquals(allJobs.get(0).getDescription(), "job1 Description 1");
        assertEquals(allJobs.get(0).getInstruction(), "job1 Instruction 1");
        assertNull(allJobs.get(0).getRecruiterName());
        assertNull(allJobs.get(0).getAdCompany());
        assertNull(allJobs.get(0).getAdEmail());
        assertNull(allJobs.get(0).getAdPhone());
        assertNull(allJobs.get(0).getApplicationDeadline());
        assertEquals(allJobs.get(1).getId(), 2L);
        assertEquals(allJobs.get(1).getTitle(), "job2 Title 2");
        assertEquals(allJobs.get(1).getDescription(), "job2 Description 2");
        assertEquals(allJobs.get(1).getInstruction(), "job2 Instruction 2");
        assertNull(allJobs.get(1).getRecruiterName());
        assertNull(allJobs.get(1).getAdCompany());
        assertNull(allJobs.get(1).getAdEmail());
        assertNull(allJobs.get(1).getAdPhone());
        assertNull(allJobs.get(1).getApplicationDeadline());
        assertEquals(allJobs.get(2).getId(), 3L);
        assertEquals(allJobs.get(2).getTitle(), "job3 Title 3");
        assertEquals(allJobs.get(2).getDescription(), "job3 Description 3");
        assertEquals(allJobs.get(2).getInstruction(), "job3 Instruction 3");
        assertNull(allJobs.get(2).getRecruiterName());
        assertNull(allJobs.get(2).getAdCompany());
        assertNull(allJobs.get(2).getAdEmail());
        assertNull(allJobs.get(2).getAdPhone());
        assertNull(allJobs.get(2).getApplicationDeadline());
        assertEquals(allJobs.get(3).getId(), 4L);
        assertEquals(allJobs.get(3).getTitle(), "Fullstack Utvecklare");
        assertEquals(allJobs.get(3).getDescription(), "Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år.");
        assertEquals(allJobs.get(3).getInstruction(), "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.");
        assertNull(allJobs.get(3).getRecruiterName());
        assertNull(allJobs.get(3).getAdCompany());
        assertNull(allJobs.get(3).getAdEmail());
        assertNull(allJobs.get(3).getAdPhone());
        assertNull(allJobs.get(3).getApplicationDeadline());
    }

    @Test
    @DisplayName("getAllJobDtos - Success")
    void test_GetAllJobDtos_Success() {
        List<JobDtoView> allJobDtos = this.jobService.getAllJobDtos();

        assertEquals(allJobDtos.size(), 4);

        assertEquals(allJobDtos.get(0).title(), "job1 Title 1");
        assertEquals(allJobDtos.get(0).description(), "job1 Description 1");
        assertNull(allJobDtos.get(0).recruiterName());
        assertNull(allJobDtos.get(0).adCompany());
        assertNull(allJobDtos.get(0).adEmail());
        assertNull(allJobDtos.get(0).adPhone());
        assertNull(allJobDtos.get(0).applicationDeadline());
        assertEquals(allJobDtos.get(1).title(), "job2 Title 2");
        assertEquals(allJobDtos.get(1).description(), "job2 Description 2");
        assertNull(allJobDtos.get(1).recruiterName());
        assertNull(allJobDtos.get(1).adCompany());
        assertNull(allJobDtos.get(1).adEmail());
        assertNull(allJobDtos.get(1).adPhone());
        assertNull(allJobDtos.get(1).applicationDeadline());
        assertEquals(allJobDtos.get(2).title(), "job3 Title 3");
        assertEquals(allJobDtos.get(2).description(), "job3 Description 3");
        assertNull(allJobDtos.get(2).recruiterName());
        assertNull(allJobDtos.get(2).adCompany());
        assertNull(allJobDtos.get(2).adEmail());
        assertNull(allJobDtos.get(2).adPhone());
        assertNull(allJobDtos.get(2).applicationDeadline());
    }

    @Test
    @DisplayName("getAllJobDtosByUserEmail - Success")
    void test_GetAllJobsByUserEmail_Success() {
        String email = "user1-h2@hh.se";
        List<JobDtoView> allJobDtos = this.jobService.getAllJobDtosByUserEmail(email);

        assertEquals(2, allJobDtos.size());
        assertEquals(allJobDtos.get(0).title(), "job1 Title 1");
        assertEquals(allJobDtos.get(0).description(), "job1 Description 1");
        assertNull(allJobDtos.get(0).recruiterName());
        assertNull(allJobDtos.get(0).adCompany());
        assertNull(allJobDtos.get(0).adEmail());
        assertNull(allJobDtos.get(0).adPhone());
        assertNull(allJobDtos.get(0).applicationDeadline());
        assertEquals(allJobDtos.get(1).title(), "job2 Title 2");
        assertEquals(allJobDtos.get(1).description(), "job2 Description 2");
        assertNull(allJobDtos.get(1).recruiterName());
        assertNull(allJobDtos.get(1).adCompany());
        assertNull(allJobDtos.get(1).adEmail());
        assertNull(allJobDtos.get(1).adPhone());
        assertNull(allJobDtos.get(1).applicationDeadline());
    }

    @Test
    @DisplayName("getAllJobCardsByUserEmail - Success")
    void test_GetAllJobCardsByUserEmail_Success() {
        String email = "user1-h2@hh.se";
        List<JobCardDtoView> allJobs = this.jobService.getAllJobCardsByUserEmail(email);

        assertEquals(2, allJobs.size());
        assertEquals(allJobs.get(0).id(), 1L);
        assertEquals(allJobs.get(0).title(), "job1 Title 1");
        assertEquals(allJobs.get(0).applicationDeadline(), "job1 applicationDeadline 1");
        assertEquals(allJobs.get(1).id(), 2L);
        assertEquals(allJobs.get(1).title(), "job2 Title 2");
        assertEquals(allJobs.get(1).applicationDeadline(), "job2 applicationDeadline 2");
    }

    @Test
    @DisplayName("findById - Success")
    void test_FindById_Success() {
        long jobId = 1L;

        Job foundJob = this.jobService.findById(jobId);

        assertEquals(foundJob.getId(), 1L);
        assertEquals(foundJob.getTitle(), "job1 Title 1");
        assertEquals(foundJob.getDescription(), "job1 Description 1");
        assertEquals(foundJob.getInstruction(), "job1 Instruction 1");
        assertNull(foundJob.getRecruiterName());
        assertNull(foundJob.getAdCompany());
        assertNull(foundJob.getAdEmail());
        assertNull(foundJob.getAdPhone());
        assertNull(foundJob.getApplicationDeadline());
    }

    @Test
    @DisplayName("findById - Invalid Id (Exception)")
    void test_FindById_InvalidId_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.findById(Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("getFullJobDtoByJobId - Success")
    void test_GetFullJobDtoByJobId_Success() {
        long jobId = 1L;
        JobDtoView jobDtoView = this.jobService.getFullJobDtoByJobId(jobId);

        assertEquals(jobDtoView.title(), "job1 Title 1");
        assertEquals(jobDtoView.description(), "job1 Description 1");
        assertNull(jobDtoView.recruiterName());
        assertNull(jobDtoView.adCompany());
        assertNull(jobDtoView.adEmail());
        assertNull(jobDtoView.adPhone());
        assertNull(jobDtoView.applicationDeadline());
    }

    @Test
    @DisplayName("getFullJobDtoByJobId - Invalid Job Id - Exception")
    void test_GetFullJobDtoByJobId_InvalidId() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.getFullJobDtoByJobId(Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("addJob - Success")
    void test_AddJob_Success() {
        List<Job> all = this.jobService.findAll();
        for (Job job : all) {
            System.out.println(job.getId());
        }

        String email = "user1-h2@hh.se";
        Job newJob = new Job(
                "job5 Title 5",
                "job5 Description 5",
                "job5 Instruction 5");

        Job addedJob = this.jobService.addJob(email, newJob);

        assertEquals(addedJob.getId(), 5L);
        assertEquals(addedJob.getTitle(), "job5 Title 5");
        assertEquals(addedJob.getDescription(), "job5 Description 5");
        assertEquals(addedJob.getInstruction(), "job5 Instruction 5");
        assertNull(addedJob.getRecruiterName());
        assertNull(addedJob.getAdCompany());
        assertNull(addedJob.getAdEmail());
        assertNull(addedJob.getAdPhone());
        assertNull(addedJob.getApplicationDeadline());

        Optional<AccountDtoView> foundAccountDto = this.accountRepository.getAccountDtoByEmail(email);

        if (foundAccountDto.isPresent()) {

            assertEquals(foundAccountDto.get().number_of_jobs(), 3);
            assertEquals(foundAccountDto.get().email(), "user1-h2@hh.se");
            assertEquals(foundAccountDto.get().roles(), "user");
        }
    }

    @Test
    @DisplayName("addJob - Invalid Input - (Exception")
    void test_AddJobb_InvalidSecondParameter_Exception() {
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
    @DisplayName("addJob - Null Form - Exception")
    void test_AddJobNullJobDtoFormAdd_NullForm_Exception() {
        String email = "user1-h2@hh.se";

        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.jobService.addJob(email, null));

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Job can't be null");
    }

    @Test
    @DisplayName("update - Success")
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
                "updated applicationDeadline"
        );

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
    @DisplayName("update - Null Form - Exception")
    void testUpdateNullJobDtoFormUpdate() {
        long jobId = 1L;

        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.jobService.update(jobId, null));

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Update can't be null");
    }

    @Test
    @DisplayName("update - Invalid Input - Exception")
    void test_Update_InvalidInput_Exception() {
        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "updated title",
                "updated description",
                "updated instruction",
                "updated recruiterName",
                "updated adCompany",
                "updated adEmail",
                "updated adPhone",
                "updated applicationDeadline"
        );

        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.update(Long.MAX_VALUE, jobDtoFormUpdate));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    // Delete Success test can be found in JobServiceComplementaryH2Test

    @Test
    @DisplayName("delete - Invalid Account Id Input - Exception")
    void test_Delete_InvalidAccountIdInput_Success() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.delete("Invalid Email", 1L));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("delete - Invalid Job Id Input - Exception")
    void test_Delete_InvalidJobIdInput_Success() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.delete("user1-h2@hh.se", Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }
}
