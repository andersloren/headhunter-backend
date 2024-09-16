package se.sprinta.headhunterbackend.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.TestUtils;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.job.converter.JobDtoFormAddToJobConverter;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("mock-test")
@WebMvcTest(JobController.class)
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class JobControllerMockTest {

    @MockBean
    private JobService jobService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobDtoFormAddToJobConverter jobDtoFormAddToJobConverter;

    @Value("${api.endpoint.base-url-job}")
    String baseUrlJob;

    private List<Account> accounts = new ArrayList<>();
    private List<Job> jobs = new ArrayList<>();
    private List<JobDtoView> jobDtos = new ArrayList<>();
    private JobDtoFormAdd jobDtoFormAdd;
    private JobDtoFormUpdate jobDtoFormUpdate;

    @BeforeEach
    void setUp() {
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
        this.jobs = MockDatabaseInitializer.initializeMockJobs();
        this.jobDtos = MockDatabaseInitializer.initializeMockJobDtos();
        this.jobDtoFormAdd = MockDatabaseInitializer.initializeMockJobDtoFormAdd();
        this.jobDtoFormUpdate = MockDatabaseInitializer.initializeMockJobDtoFormUpdate();
    }

    @Test
    @DisplayName("Test Data Initializer")
    void test_DataInitializer() {
        System.out.println("JobControllerMockTest, jobs size: " + this.jobs.size());
        System.out.println(this.jobs.get(0).getId());
        for (Job job : this.jobs) {
            System.out.println(job.toString());
        }
        System.out.println("JobControllerMockTest, jobDtos size: " + this.jobDtos.size());
        for (JobDtoView jobDto : this.jobDtos) {
            System.out.println(jobDto.toString());
        }
    }

    @Test
    @DisplayName("GET - findAll - Success")
    void testFindAllJobs() throws Exception {
        // Given
        given(this.jobService.findAll()).willReturn(this.jobs);

        // When and Then;
        ResultActions result = this.mockMvc.perform(get(this.baseUrlJob + "/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Jobs Success"));

        JsonNode dataArray = TestUtils.getJSONResponse(result);

        for (int i = 0; i < dataArray.size(); i++) {
            result.andExpect(jsonPath("$.data[" + i + "].id").value(this.jobs.get(i).getId()))
                    .andExpect(jsonPath("$.data[" + i + "].title").value(this.jobs.get(i).getTitle()))
                    .andExpect(jsonPath("$.data[" + i + "].description").value(this.jobs.get(i).getDescription()))
                    .andExpect(jsonPath("$.data[" + i + "].instruction").value(this.jobs.get(i).getInstruction()))
                    .andExpect(jsonPath("$.data[" + i + "].recruiterName").value(this.jobs.get(i).getRecruiterName()))
                    .andExpect(jsonPath("$.data[" + i + "].adCompany").value(this.jobs.get(i).getAdCompany()))
                    .andExpect(jsonPath("$.data[" + i + "].adEmail").value(this.jobs.get(i).getAdEmail()))
                    .andExpect(jsonPath("$.data[" + i + "].adPhone").value(this.jobs.get(i).getAdPhone()))
                    .andExpect(jsonPath("$.data[" + i + "].account.email").value(this.jobs.get(i).getAccount().getEmail()));
        }
    }

    @Test
    @DisplayName("GET - findById - Success")
    void testFindByIdSuccess() throws Exception {

        System.out.println(this.jobs.get(0).getId());

        // Given
        given(this.jobService.findById(1L)).willReturn(this.jobs.get(0));

        // When and Then
        this.mockMvc.perform(get(this.baseUrlJob + "/findById/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(this.jobs.get(0).getId()))
                .andExpect(jsonPath("$.data.title").value(this.jobs.get(0).getTitle()))
                .andExpect(jsonPath("$.data.description").value(this.jobs.get(0).getDescription()))
                .andExpect(jsonPath("$.data.instruction").value(this.jobs.get(0).getInstruction()))
                .andExpect(jsonPath("$.data.recruiterName").value(this.jobs.get(0).getRecruiterName()))
                .andExpect(jsonPath("$.data.adCompany").value(this.jobs.get(0).getAdCompany()))
                .andExpect(jsonPath("$.data.adEmail").value(this.jobs.get(0).getAdEmail()))
                .andExpect(jsonPath("$.data.adPhone").value(this.jobs.get(0).getAdPhone()))
                .andExpect(jsonPath("$.data.account.email").value(this.jobs.get(0).getAccount().getEmail()));
    }

    @Test
    @DisplayName("GET - findById - Non-existent Id - Exception")
    void testFindByIdWithNonExistentId() throws Exception {
        // Given
        given(this.jobService.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and then
        this.mockMvc.perform(get(this.baseUrlJob + "/findById/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("POST - addJob - Success")
    void testAddJobSuccess() throws Exception {

        Account user = this.accounts.get(0);

        JobDtoFormAdd newJobDtoFormAdd = this.jobDtoFormAdd;

        Job newJob = new Job();
        newJob.setTitle(newJobDtoFormAdd.title());
        newJob.setDescription(newJobDtoFormAdd.description());
        newJob.setInstruction(newJobDtoFormAdd.instruction());

        Job savedJob = new Job();
        savedJob.setId(4L);
        savedJob.setTitle("Title Mock");
        savedJob.setDescription("Description Mock");
        savedJob.setInstruction("Instruction Mock");
        savedJob.setAccount(user);

        String json = this.objectMapper.writeValueAsString(newJobDtoFormAdd);

        // Given
        given(this.jobDtoFormAddToJobConverter.convert(newJobDtoFormAdd)).willReturn(newJob);
        given(this.jobService.addJob("user1-mock@hh.se", newJob)).willReturn(savedJob);

        // When and then
        this.mockMvc.perform(post(this.baseUrlJob + "/addJob" + "/user1-mock@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.title").value("Title Mock"))
                .andExpect(jsonPath("$.data.description").value("Description Mock"))
                .andExpect(jsonPath("$.data.recruiterName").isEmpty())
                .andExpect(jsonPath("$.data.adCompany").isEmpty())
                .andExpect(jsonPath("$.data.adEmail").isEmpty())
                .andExpect(jsonPath("$.data.adPhone").isEmpty());
    }

    @Test
    @DisplayName("PUT - update - Success")
    void test_UpdateJob_Success() throws Exception {
        Account account = new Account();
        account.setEmail("user@hh.se");
        account.setRoles("user");

        JobDtoFormUpdate jobDtoFormUpdate = this.jobDtoFormUpdate;

        String json = this.objectMapper.writeValueAsString(jobDtoFormUpdate);

        Job update = new Job();
        update.setId(1L);
        update.setAccount(account);
        update.setTitle(jobDtoFormUpdate.title());
        update.setDescription(jobDtoFormUpdate.description());
        update.setInstruction(jobDtoFormUpdate.instruction());
        update.setRecruiterName(jobDtoFormUpdate.recruiterName());
        update.setAdCompany(jobDtoFormUpdate.adCompany());
        update.setAdEmail(jobDtoFormUpdate.adEmail());
        update.setAdPhone(jobDtoFormUpdate.adPhone());
        update.setApplicationDeadline(jobDtoFormUpdate.applicationDeadline());

        // Given
        given(this.jobService.update(1L, jobDtoFormUpdate)).willReturn(update);

        // When and then
        this.mockMvc.perform(put(this.baseUrlJob + "/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.title").value("Updated Title Mock"))
                .andExpect(jsonPath("$.data.description").value("Updated Description Mock"))
                .andExpect(jsonPath("$.data.recruiterName").value("Updated RecruiterName Mock"))
                .andExpect(jsonPath("$.data.adCompany").value("Updated adCompany Mock"))
                .andExpect(jsonPath("$.data.adEmail").value("Updated adEmail Mock"))
                .andExpect(jsonPath("$.data.adPhone").value("Updated adPhone Mock"))
                .andExpect(jsonPath("$.data.applicationDeadline").value("Updated adApplicationDeadline Mock"));
    }

    @Test
    @DisplayName("PUT - update - Non-Existent Email - Exception")
    void test_UpdateJob_NonExistentId_Exception() throws Exception {
        // Given
        JobDtoFormUpdate jobDtoFormUpdate = this.jobDtoFormUpdate;

        String json = this.objectMapper.writeValueAsString(jobDtoFormUpdate);

        given(this.jobService.update(eq(Long.MAX_VALUE), eq(jobDtoFormUpdate)))
                .willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When and then
        this.mockMvc.perform(put(this.baseUrlJob + "/update/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE - delete - Success")
    void test_Delete_Success() throws Exception {
        // Given
        doNothing().when(this.jobService).delete("user@hh.se", 1L);

        // When and Then
        this.mockMvc.perform(delete(this.baseUrlJob + "/delete" + "/user@hh.se" + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE - delete - Non-existent Email - Exception")
    void test_Delete_NonExistentId_Exception() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("job", Long.MAX_VALUE)).when(this.jobService).delete("user1@hh.se",
                Long.MAX_VALUE);

        this.mockMvc.perform(delete(this.baseUrlJob + "/delete" + "/user1@hh.se/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE - delete - Non-Existent Job Id - Exception")
    void test_Delete_NonExistentEmail_Exception() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("account", "Invalid Email")).when(this.jobService).delete("Invalid Email", 1L);

        this.mockMvc.perform(delete(this.baseUrlJob + "/delete" + "/Invalid Email" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
