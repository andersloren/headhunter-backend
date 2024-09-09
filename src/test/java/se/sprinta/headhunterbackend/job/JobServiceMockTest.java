package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountRepository;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.AdRepository;
import se.sprinta.headhunterbackend.client.chat.ChatClient;
import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;
import se.sprinta.headhunterbackend.client.chat.dto.Choice;
import se.sprinta.headhunterbackend.client.chat.dto.Message;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.system.exception.DoesNotExistException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.utils.HtmlUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceMockTest {

  @Mock
  private JobRepository jobRepository;
  @Mock
  private AdRepository adRepository;
  @Mock
  private AccountRepository accountRepository;
  @Mock
  private ChatClient chatClient;
  @Mock
  private HtmlUtilities htmlUtilities;

  @InjectMocks
  private JobService jobService;

  private List<Job> jobs = new ArrayList<>();
  private List<JobDtoView> jobDtos = new ArrayList<>();
  private List<JobCardDtoView> jobCardDtoViews = new ArrayList<>();
  private List<Account> accounts = new ArrayList<>();

  @BeforeEach
  void setUp() {
    this.jobs = MockDatabaseInitializer.initializeMockJobs();
    this.jobDtos = MockDatabaseInitializer.initializeMockJobDtos();
    this.jobCardDtoViews = MockDatabaseInitializer.initializeMockJobCardDtos();
    this.accounts = MockDatabaseInitializer.initializeMockAccounts();
  }

  @Test
  @DisplayName("Test Data Initializer")
  void test_DataInitializer() {
    System.out.println("JobServiceMockTest, jobs size: " + this.jobs.size());
    for (Job job : this.jobs) {
      System.out.println(job.toString());
    }
    System.out.println("JobServiceMockTest, jobDtos size: " + this.jobDtos.size());
    for (JobDtoView jobDto : this.jobDtos) {
      System.out.println(jobDto.toString());
    }
    System.out.println("JobServiceMockTest, jobCardDtoViews size: " + this.jobCardDtoViews.size());
    for (JobCardDtoView jobCardDto : this.jobCardDtoViews) {
      System.out.println(jobCardDto.toString());
    }
  }

  @Test
  @DisplayName("GET - findAll - Success")
  void testFindAllJobs() {
    // Given
    given(this.jobRepository.findAll()).willReturn(this.jobs);

    // When
    List<Job> actualJobs = this.jobService.findAll();

    // Then
    assertThat(actualJobs.size()).isEqualTo(this.jobs.size());

    // Verify
    then(this.jobRepository).should().findAll();
  }

  @Test
  @DisplayName("GET - getJobDtos - Success")
  void testGetAllJobDtos() {
    // Given
    given(this.jobRepository.getJobDtos()).willReturn(this.jobDtos);

    // When
    List<JobDtoView> actualJobDtos = this.jobService.getJobDtos();

    // Then
    assertThat(actualJobDtos.size()).isEqualTo(this.jobDtos.size());

    // Verify
    then(this.jobRepository).should().getJobDtos();
  }

  @Test
  @DisplayName("GET - getJobDtosByEmail - Success")
  void test_GetJobsByEmail() {
    Account account = new Account();
    account.setEmail("user1-mock@hh.se");
    account.setPassword("a");
    account.setRoles("user");

    // Given
    given(this.accountRepository.findById("user1-mock@hh.se")).willReturn(Optional.of(account));
    given(this.jobRepository.getJobDtosByEmail("user1-mock@hh.se")).willReturn(this.jobDtos);

    // When
    List<JobDtoView> foundJobs = this.jobService.getJobDtosByEmail("user1-mock@hh.se");

    // Then
    assertEquals(foundJobs.size(), this.jobs.size());

    // Verify
    then(this.jobRepository).should().getJobDtosByEmail("user1-mock@hh.se");
  }

  @Test
  @DisplayName("GET - getJobDtosByEmail - Non-existent Email - Exception")
  void test_GetJobDtosByEmail_NonExistentEmail_Exception() {
    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.getJobDtosByEmail("abc"));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find account with Email abc");
  }

  @Test
  @DisplayName("GET - getJobCardDtosByEmail - Success")
  void test_GetJobCardsByEmail_Success() {
    List<Job> filteredJobs = this.jobs.stream()
        .filter(job -> job.getAccount().getEmail().equals("user1-mock@hh.se"))
        .toList();

    System.out.println(filteredJobs);

    // Given
    given(this.accountRepository.findById("user1-mock@hh.se")).willReturn(Optional.of(this.accounts.get(0)));
    given(this.jobRepository.getJobCardDtosByEmail("user1-mock@hh.se")).willReturn(List.of(
        this.jobCardDtoViews.get(0),
        this.jobCardDtoViews.get(1)));

    // When
    List<JobCardDtoView> jobCardDtos = this.jobService.getJobCardDtosByEmail("user1-mock@hh.se");

    // Then
    assertEquals(jobCardDtos.size(), filteredJobs.size());

    // Verify
    then(this.jobRepository).should().getJobCardDtosByEmail("user1-mock@hh.se");
  }

  @Test
  @DisplayName("GET - getJobDtoDtosByEmail - Non-existent Email - Exception")
  void test_GetJobDtoDtosByEmail_NonExistentEmail_Exception() {
    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.getJobDtosByEmail("abc"));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find account with Email abc");
  }

  @Test
  @DisplayName("GET - findByID - Success")
  void test_FindByIdSuccess() {
    Job job = new Job();

    // Given
    given(this.jobRepository.findById(1L)).willReturn(Optional.of(job));

    // When
    Job foundJob = this.jobService.findById(1L);

    // Then
    assertEquals(foundJob.getTitle(), job.getTitle());

    // Verify
    then(this.jobRepository).should().findById(1L);
  }

  @DisplayName("GET - findByID - Invalid Id - Exception")
  @Test
  void test_FindJobByInvalidId() {
    // Given
    given(this.jobRepository.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.findById(Long.MAX_VALUE));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

    // Verify
    then(this.jobRepository).should().findById(Long.MAX_VALUE);
  }

  @Test
  @DisplayName("GET - getJobDto - Success")
  void test_GetJobDto_Success() {
    JobDtoView jobDtoView = new JobDtoView(
        "Title",
        "Description",
        "Recruiter Name",
        "adCompany",
        "adEmail",
        "adPhone",
        "applicationDeadline");

    // Given
    given(this.jobRepository.getJobDto(Mockito.anyLong())).willReturn(Optional.of(jobDtoView));

    // When
    JobDtoView foundJobDtoView = this.jobService.getJobDto(Mockito.anyLong());

    // Then
    assertEquals(foundJobDtoView, jobDtoView);

    // Verify
    then(this.jobRepository).should().getJobDto(Mockito.anyLong());
  }

  @Test
  @DisplayName("GET - getJobDto - Invalid Id - Exception")
  void test_GetJobDto_InvalidId_Exception() {
    // Given
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.getJobDto(Long.MAX_VALUE));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

    // Verify
    then(this.jobRepository).should().getJobDto(Long.MAX_VALUE);

  }

  @Test
  @DisplayName("POST - addJob - Success")
  void test_AddJob() {

    Job newJob = new Job(
        "title",
        "description",
        "instruction");

    ArgumentCaptor<Job> jobArgumentCaptor = forClass(Job.class);

    Account account = new Account();
    account.setEmail("email");

    // Given
    given(this.accountRepository.findAccountByEmail("email")).willReturn(Optional.of(account));
    given(this.jobRepository
        .save(jobArgumentCaptor.capture()))
        .willAnswer(invocation -> invocation.getArgument(0));

    // When
    Job savedJob = this.jobService.addJob("email", newJob);

    // Then
    Job capturedJob = jobArgumentCaptor.getValue();

    assertEquals(savedJob, jobArgumentCaptor.getValue());

    assertEquals(savedJob.getTitle(), capturedJob.getTitle());
    assertEquals(savedJob.getDescription(), capturedJob.getDescription());
    assertEquals(savedJob.getInstruction(), capturedJob.getInstruction());

    // Verify
    then(this.accountRepository).should().findAccountByEmail("email");
    then(this.jobRepository).should().save(jobArgumentCaptor.capture());
  }

  @Test
  @DisplayName("POST - addJob - Null Input - Exception")
  void test_AddJob_InvalidInput_Exception() {
    // Given
    Throwable thrown = assertThrows(NullPointerException.class,
        () -> this.jobService.addJob("user1-mock@hh.se", null));

    // Then
    assertThat(thrown)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Job can't be null");
  }

  @Test
  @DisplayName("PUT - update - Success")
  void test_UpdateSuccess() {
    JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
        "updated title",
        "updated description",
        "updated instruction",
        "recruiterName",
        "adCompany",
        "adEmail",
        "adPhone",
        "applicationDeadline");

    ArgumentCaptor<Job> jobArgumentCaptor = forClass(Job.class);

    Job foundJob = new Job();

    // Given
    given(this.jobRepository.findById(1L)).willReturn(Optional.of(foundJob));
    given(this.jobRepository
        .save(jobArgumentCaptor.capture()))
        .willAnswer(invocation -> invocation.getArgument(0));

    // When
    Job savedJob = this.jobService.update(1L, jobDtoFormUpdate);
    Job capturedJob = jobArgumentCaptor.getValue();

    // Then
    assertEquals(savedJob.getTitle(), capturedJob.getTitle());
    assertEquals(savedJob.getDescription(), capturedJob.getDescription());
    assertEquals(savedJob.getInstruction(), capturedJob.getInstruction());
    assertEquals(savedJob.getRecruiterName(), capturedJob.getRecruiterName());
    assertEquals(savedJob.getAdCompany(), capturedJob.getAdCompany());
    assertEquals(savedJob.getAdEmail(), capturedJob.getAdEmail());
    assertEquals(savedJob.getAdPhone(), capturedJob.getAdPhone());
    assertEquals(savedJob.getApplicationDeadline(), capturedJob.getApplicationDeadline());

    // Verify
    then(this.jobRepository).should().findById(1L);
    then(this.jobRepository).should().save(jobArgumentCaptor.capture());
  }

  @Test
  @DisplayName("PUT - update - Null Input - Exception")
  void test_UpdateNullUpdate() {
    // When
    Throwable thrown = assertThrows(NullPointerException.class,
        () -> this.jobService.update(1L, null));

    // Then
    assertThat(thrown)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Update can't be null");
  }

  @Test
  @DisplayName("PUT - update - Invalid Id - Exception")
  void test_UpdateInvalidId() {
    JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
        "updated title",
        "updated description",
        "updated instruction",
        "recruiterName",
        "adCompany",
        "adEmail",
        "adPhone",
        "applicationDeadline");

    // Given
    given(this.jobRepository.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.update(Long.MAX_VALUE, jobDtoFormUpdate));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

    // Verify
    then(this.jobRepository).should().findById(Long.MAX_VALUE);
  }

  @Test
  @DisplayName("DELETE - delete - Success")
  void test_Delete() {
    // Given
    given(this.accountRepository.findById("user1-mock@hh.se")).willReturn(Optional.of(this.accounts.get(0)));
    given(this.jobRepository.findById(1L)).willReturn(Optional.of(this.jobs.get(0)));
    willDoNothing().given(this.jobRepository).delete(this.jobs.get(0));

    // When
    this.jobService.delete("user1-mock@hh.se", 1L);

    // Verify
    then(this.accountRepository).should().findById("user1-mock@hh.se");
    then(this.jobRepository).should().findById(1L);
  }

  @Test
  @DisplayName("DELETE - delete - Invalid Email - Exception")
  void test_DeleteWithInvalidUserEmail() {
    // Given
    given(this.accountRepository.findById("abc")).willThrow(new ObjectNotFoundException("account", "abc"));

    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.delete("abc", 1L));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find account with Email abc");

    // Verify
    then(this.accountRepository).should().findById("abc");
  }

  @Test
  @DisplayName("DELETE - delete - Invalid Job Id - Exception")
  void test_DeleteWithInvalidId() {
    // Given
    given(this.accountRepository.findById("user1-mock@hh.se")).willReturn(Optional.of(this.accounts.get(0)));
    given(this.jobRepository.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.delete("user1-mock@hh.se", Long.MAX_VALUE));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

    // Verify
    then(this.accountRepository).should().findById("user1-mock@hh.se");
    then(this.jobRepository).should().findById(Long.MAX_VALUE);
  }

  @Test
  @DisplayName("POST - generate - Success")
  void test_GenerateSuccess() {
    Job job = new Job();
    job.setId(1L);
    job.setInstruction("instruction");
    job.setDescription("description");

    String response = "<!DOCTYPE html><html><body>generate content</body></html>";

    Message systemMessage = new Message("system", job.getInstruction());
    Message userMessage = new Message("user", job.getDescription());
    ChatRequest chatRequest = new ChatRequest("gpt-4o", List.of(systemMessage, userMessage));

    Choice choice = mock(Choice.class);
    when(choice.message()).thenReturn(new Message(
        "assistant",
        "<!DOCTYPE html><html><body>generate content</body></html>"));
    List<Choice> choices = List.of(choice);
    ChatResponse chatResponse = new ChatResponse(choices);

    Ad ad = new Ad();
    ad.setHtmlCode("<!DOCTYPE html><html><body>generate content</body></html>");

    job.addAd(ad);

    // Given
    given(this.jobRepository.findById(1L)).willReturn(Optional.of(job));
    given(this.chatClient.generate(chatRequest)).willReturn(chatResponse);
    given(this.htmlUtilities.makeHtmlResponseSubstring(response)).willReturn(response);
    given(this.adRepository.save(ad)).willReturn(ad);

    // When
    String substringResponse = this.jobService.generate(job.getId());

    // Then
    ArgumentCaptor<ChatRequest> chatRequestArgumentCaptor = ArgumentCaptor.forClass(ChatRequest.class);
    ArgumentCaptor<Ad> adArgumentCaptor = ArgumentCaptor.forClass(Ad.class);

    // Verify
    then(this.jobRepository).should().findById(job.getId());
    verify(this.chatClient).generate(chatRequestArgumentCaptor.capture());
    then(this.adRepository).should().save(adArgumentCaptor.capture());

    // Assert
    ChatRequest capturedChatRequest = chatRequestArgumentCaptor.getValue();
    List<Message> capturedMessages = capturedChatRequest.messages();
    assertEquals(2, capturedMessages.size());
    assertEquals("system", capturedMessages.get(0).role());
    assertEquals("instruction", capturedMessages.get(0).content());
    assertEquals("user", capturedMessages.get(1).role());

    Ad capturedAd = adArgumentCaptor.getValue();
    assertEquals("<!DOCTYPE html><html><body>generate content</body></html>", capturedAd.getHtmlCode());
    assertEquals("<!DOCTYPE html><html><body>generate content</body></html>", substringResponse);
  }

  @Test
  @DisplayName("POST - generate - Invalid Job Id - Exception")
  void test_Generate_InvalidJobId_Exception() {
    // Given
    given(this.jobRepository.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class,
        () -> this.jobService.generate(Long.MAX_VALUE));

    // Then
    assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

    // Verify
    then(this.jobRepository).should().findById(Long.MAX_VALUE);
  }
}
