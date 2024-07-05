package se.sprinta.headhunterbackend.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.job.converter.JobDtoFormAddToJobConverter;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.DoesNotExistException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
@ActiveProfiles("test-h2")
class JobControllerMockTest {

    @MockBean
    private JobService jobService;

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Job> jobs = new ArrayList<>();

    @MockBean
    private JobDtoFormAddToJobConverter jobDtoFormAddToJobConverter;

    @Value("${api.endpoint.base-url-job}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        Account admin = new Account();
        admin.setEmail("admin@hh.se");
        admin.setPassword("a");
        admin.setRoles("admin");

        Account user1 = new Account();
        user1.setEmail("user1@hh.se");
        user1.setRoles("user");

        Account user2 = new Account();
        user2.setEmail("user2@hh.se");
        user2.setRoles("user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Title job1 test-h2");
        job1.setDescription("Description job1 test-h2");
        job1.setInstruction("Instruction job1 test-h2");
        job1.setRecruiterName("RecruiterName job1 test-h2");
        job1.setAdCompany("Company job1 test-h2");
        job1.setAdEmail("Email job1 test-h2");
        job1.setAdPhone("Phone job1 test-h2");
        job1.setApplicationDeadline("ApplicationDeadline 1 test-h2");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setTitle("Title job2 test-h2");
        job2.setDescription("Description job2 test-h2");
        job2.setInstruction("Instruction job2 test-h2");
        job2.setRecruiterName("RecruiterName job2 test-h2");
        job2.setAdCompany("Company job2 test-h2");
        job2.setAdEmail("Email job2 test-h2");
        job2.setAdPhone("Phone job2 test-h2");
        job2.setApplicationDeadline("ApplicationDeadline 2 test-h2");

        Job job3 = new Job();
        job3.setId(3L);
        job3.setTitle("Title job3 test-h2");
        job3.setDescription("Description job3 test-h2");
        job3.setInstruction("Instruction job3 test-h2");
        job3.setRecruiterName("RecruiterName job3 test-h2");
        job3.setAdCompany("Company job3 test-h2");
        job3.setAdEmail("Email job3 test-h2");
        job3.setAdPhone("Phone job3 test-h2");
        job3.setApplicationDeadline("ApplicationDeadline job3 test-h2");

        user1.addJob(job1);
        user1.addJob(job2);
        user2.addJob(job3);

        this.jobs.add(job1);
        this.jobs.add(job2);
        this.jobs.add(job3);
    }

    @Test
    @DisplayName("findAllJobs")
    void testFindAllJobs() throws Exception {
        // Given
        given(this.jobService.findAll()).willReturn(this.jobs);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Jobs Success"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Title job1 test-h2"))
                .andExpect(jsonPath("$.data[0].description").value("Description job1 test-h2"))
                .andExpect(jsonPath("$.data[0].instruction").value("Instruction job1 test-h2"))
                .andExpect(jsonPath("$.data[0].recruiterName").value("RecruiterName job1 test-h2"))
                .andExpect(jsonPath("$.data[0].adCompany").value("Company job1 test-h2"))
                .andExpect(jsonPath("$.data[0].adEmail").value("Email job1 test-h2"))
                .andExpect(jsonPath("$.data[0].adPhone").value("Phone job1 test-h2"))
                .andExpect(jsonPath("$.data[0].account.email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("Title job2 test-h2"))
                .andExpect(jsonPath("$.data[1].description").value("Description job2 test-h2"))
                .andExpect(jsonPath("$.data[1].instruction").value("Instruction job2 test-h2"))
                .andExpect(jsonPath("$.data[1].recruiterName").value("RecruiterName job2 test-h2"))
                .andExpect(jsonPath("$.data[1].adCompany").value("Company job2 test-h2"))
                .andExpect(jsonPath("$.data[1].adEmail").value("Email job2 test-h2"))
                .andExpect(jsonPath("$.data[1].adPhone").value("Phone job2 test-h2"))
                .andExpect(jsonPath("$.data[1].account.email").value("user1@hh.se"))
                .andExpect(jsonPath("$.data[2].id").value(3L))
                .andExpect(jsonPath("$.data[2].title").value("Title job3 test-h2"))
                .andExpect(jsonPath("$.data[2].description").value("Description job3 test-h2"))
                .andExpect(jsonPath("$.data[2].instruction").value("Instruction job3 test-h2"))
                .andExpect(jsonPath("$.data[2].recruiterName").value("RecruiterName job3 test-h2"))
                .andExpect(jsonPath("$.data[2].adCompany").value("Company job3 test-h2"))
                .andExpect(jsonPath("$.data[2].adEmail").value("Email job3 test-h2"))
                .andExpect(jsonPath("$.data[2].adPhone").value("Phone job3 test-h2"))
                .andExpect(jsonPath("$.data[2].account.email").value("user2@hh.se"));
        ;
    }

    @Test
    void testFindByIdSuccess() throws Exception {
        // Given
        given(this.jobService.findById(1L)).willReturn(this.jobs.get(0));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/findById/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Title job1 test-h2"))
                .andExpect(jsonPath("$.data.description").value("Description job1 test-h2"))
                .andExpect(jsonPath("$.data.instruction").value("Instruction job1 test-h2"))
                .andExpect(jsonPath("$.data.recruiterName").value("RecruiterName job1 test-h2"))
                .andExpect(jsonPath("$.data.adCompany").value("Company job1 test-h2"))
                .andExpect(jsonPath("$.data.adEmail").value("Email job1 test-h2"))
                .andExpect(jsonPath("$.data.adPhone").value("Phone job1 test-h2"))
                .andExpect(jsonPath("$.data.account.email").value("user1@hh.se"));
    }

    @Test
    void testFindByIdWithNonExistentId() throws Exception {
        // Given
        given(this.jobService.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/findById/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddJobSuccess() throws Exception {
        Account user1 = new Account();
        user1.setEmail("user1@hh.se");
        user1.setRoles("user");

        JobDtoFormAdd newJobDtoFormAdd = new JobDtoFormAdd(
                "Title job4 test-h2",
                "Description job4 test-h2",
                "Instruction job4 test-h2");

        Job newJob = new Job();
        newJob.setTitle(newJobDtoFormAdd.title());
        newJob.setDescription(newJobDtoFormAdd.description());
        newJob.setInstruction(newJobDtoFormAdd.instruction());


        Job savedJob = new Job();
        savedJob.setId(4L);
        savedJob.setTitle("Title job4 test-h2");
        savedJob.setDescription("Description job4 test-h2");
        savedJob.setInstruction("Instruction job4 test-h2");
        savedJob.setAccount(user1);

        String json = this.objectMapper.writeValueAsString(newJobDtoFormAdd);

        // Given
        given(this.jobDtoFormAddToJobConverter.convert(newJobDtoFormAdd)).willReturn(newJob);
        given(this.jobService.addJob("user1@hh.se", newJob)).willReturn(savedJob);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/addJob" + "/user1@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.title").value("Title job4 test-h2"))
                .andExpect(jsonPath("$.data.description").value("Description job4 test-h2"))
                .andExpect(jsonPath("$.data.recruiterName").isEmpty())
                .andExpect(jsonPath("$.data.adCompany").isEmpty())
                .andExpect(jsonPath("$.data.adEmail").isEmpty())
                .andExpect(jsonPath("$.data.adPhone").isEmpty());
    }

    @Test
    void testUpdateJobSuccess() throws Exception {
        Account account = new Account();
        account.setEmail("user@hh.se");
        account.setRoles("user");

        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "Updated Title job1 test-h2",
                "Updated Description job1 test-h2",
                "Updated Instruction job1 test-h2",
                "Updated RecruiterName job1 test-h2",
                "Updated Company job1 test-h2",
                "Updated Email job1 test-h2",
                "Updated Phone job1 test-h2",
                "Updated ApplicationDeadline job1 test-h2"
        );

        String json = this.objectMapper.writeValueAsString(jobDtoFormUpdate);

        Job update = new Job();
        update.setId(1L);
        update.setAccount(account);
        update.setTitle("Updated Title job1 test-h2");
        update.setDescription("Updated Description job1 test-h2");
        update.setInstruction("Updated Instruction job1 test-h2");
        update.setRecruiterName("Updated RecruiterName job1 test-h2");
        update.setAdCompany("Updated Company job1 test-h2");
        update.setAdEmail("Updated Email job1 test-h2");
        update.setAdPhone("Updated Phone job1 test-h2");
        update.setApplicationDeadline("Updated ApplicationDeadline job1 test-h2");

        // Given
        given(this.jobService.update(1L, jobDtoFormUpdate)).willReturn(update);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.title").value("Updated Title job1 test-h2"))
                .andExpect(jsonPath("$.data.description").value("Updated Description job1 test-h2"))
                .andExpect(jsonPath("$.data.recruiterName").value("Updated RecruiterName job1 test-h2"))
                .andExpect(jsonPath("$.data.adCompany").value("Updated Company job1 test-h2"))
                .andExpect(jsonPath("$.data.adEmail").value("Updated Email job1 test-h2"))
                .andExpect(jsonPath("$.data.adPhone").value("Updated Phone job1 test-h2"))
                .andExpect(jsonPath("$.data.applicationDeadline").value("Updated ApplicationDeadline job1 test-h2"));

    }

    @Test
    void testUpdateJobWithNonExistentId() throws Exception {
        // Given
        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "Updated Title job1 test-h2",
                "Updated Description job1 test-h2",
                "Updated Instruction job1 test-h2",
                "Updated RecruiterName job1 test-h2",
                "Updated Company job1 test-h2",
                "Updated Email job1 test-h2",
                "Updated Phone job1 test-h2",
                "Updated ApplicationDeadline job1 test-h2"
        );

        String json = this.objectMapper.writeValueAsString(jobDtoFormUpdate);

        given(this.jobService.update(eq(Long.MAX_VALUE), eq(jobDtoFormUpdate))).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/update/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteSuccess() throws Exception {
        // Given
        doNothing().when(this.jobService).delete("user@hh.se", 1L);

        // When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/user@hh.se" + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNonExistentId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("job", Long.MAX_VALUE)).when(this.jobService).delete("user1@hh.se", Long.MAX_VALUE);

        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/user1@hh.se/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNonExistentEmail() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("account", "Invalid Email")).when(this.jobService).delete("Invalid Email", 1L);

        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/Invalid Email" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWithWrongEmail() throws Exception {
        // Given
        doThrow(new DoesNotExistException()).when(this.jobService).delete("user2@hh.se", 1L);

        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/user2@hh.se" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Does not exist"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}