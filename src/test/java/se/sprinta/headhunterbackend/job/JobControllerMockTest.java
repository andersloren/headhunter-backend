package se.sprinta.headhunterbackend.job;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.job.converter.JobDtoFormAddToJobConverter;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.DoesNotExistException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import java.util.ArrayList;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
  String baseUrl;

  List<Account> accounts = new ArrayList<>();
  List<Job> jobs = new ArrayList<>();
  List<JobDtoView> jobDtos = new ArrayList<>();

  @BeforeEach
  void setUp() {
    this.accounts = MockDatabaseInitializer.initializeMockAccounts();
    this.jobs = MockDatabaseInitializer.initializeMockJobs();
    this.jobDtos = MockDatabaseInitializer.initializeMockJobDtos();
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
  @DisplayName("(GET) findAllJobs - Success")
  void testFindAllJobs() throws Exception {
    // Given
    given(this.jobService.findAll()).willReturn(this.jobs);

    // When and Then
    this.mockMvc.perform(get(this.baseUrl + "/findAll").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Find All Jobs Success"))
        .andExpect(jsonPath("$.data[0].id").value(this.jobs.get(0).getId()))
        .andExpect(jsonPath("$.data[0].title").value(this.jobs.get(0).getTitle()))
        .andExpect(jsonPath("$.data[0].description").value(this.jobs.get(0).getDescription()))
        .andExpect(jsonPath("$.data[0].instruction").value(this.jobs.get(0).getInstruction()))
        .andExpect(jsonPath("$.data[0].recruiterName").value(this.jobs.get(0).getRecruiterName()))
        .andExpect(jsonPath("$.data[0].adCompany").value(this.jobs.get(0).getAdCompany()))
        .andExpect(jsonPath("$.data[0].adEmail").value(this.jobs.get(0).getAdEmail()))
        .andExpect(jsonPath("$.data[0].adPhone").value(this.jobs.get(0).getAdPhone()))
        .andExpect(jsonPath("$.data[0].account.email").value(this.jobs.get(0).getAccount().getEmail()))
        .andExpect(jsonPath("$.data[1].id").value(this.jobs.get(1).getId()))
        .andExpect(jsonPath("$.data[1].title").value(this.jobs.get(1).getTitle()))
        .andExpect(jsonPath("$.data[1].description").value(this.jobs.get(1).getDescription()))
        .andExpect(jsonPath("$.data[1].instruction").value(this.jobs.get(1).getInstruction()))
        .andExpect(jsonPath("$.data[1].recruiterName").value(this.jobs.get(1).getRecruiterName()))
        .andExpect(jsonPath("$.data[1].adCompany").value(this.jobs.get(1).getAdCompany()))
        .andExpect(jsonPath("$.data[1].adEmail").value(this.jobs.get(1).getAdEmail()))
        .andExpect(jsonPath("$.data[1].adPhone").value(this.jobs.get(1).getAdPhone()))
        .andExpect(jsonPath("$.data[1].account.email").value(this.jobs.get(1).getAccount().getEmail()))
        .andExpect(jsonPath("$.data[2].id").value(this.jobs.get(2).getId()))
        .andExpect(jsonPath("$.data[2].title").value(this.jobs.get(2).getTitle()))
        .andExpect(jsonPath("$.data[2].description").value(this.jobs.get(2).getDescription()))
        .andExpect(jsonPath("$.data[2].instruction").value(this.jobs.get(2).getInstruction()))
        .andExpect(jsonPath("$.data[2].recruiterName").value(this.jobs.get(2).getRecruiterName()))
        .andExpect(jsonPath("$.data[2].adCompany").value(this.jobs.get(2).getAdCompany()))
        .andExpect(jsonPath("$.data[2].adEmail").value(this.jobs.get(2).getAdEmail()))
        .andExpect(jsonPath("$.data[2].adPhone").value(this.jobs.get(2).getAdPhone()))
        .andExpect(jsonPath("$.data[2].account.email").value(this.jobs.get(2).getAccount().getEmail()));
  }

  @Test
  void testFindByIdSuccess() throws Exception {
    System.out.println(this.jobs.get(0).getId());
    // Given
    given(this.jobService.findById(1L)).willReturn(this.jobs.get(0));

    // When and Then
    this.mockMvc.perform(get(this.baseUrl + "/findById/1").accept(MediaType.APPLICATION_JSON))
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
        "Updated ApplicationDeadline job1 test-h2");

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
        "Updated ApplicationDeadline job1 test-h2");

    String json = this.objectMapper.writeValueAsString(jobDtoFormUpdate);

    given(this.jobService.update(eq(Long.MAX_VALUE), eq(jobDtoFormUpdate)))
        .willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

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
    doThrow(new ObjectNotFoundException("job", Long.MAX_VALUE)).when(this.jobService).delete("user1@hh.se",
        Long.MAX_VALUE);

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
