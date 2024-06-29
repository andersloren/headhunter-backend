package se.sprinta.headhunterbackend.job;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import se.sprinta.headhunterbackend.account.AccountService;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.client.chat.ChatClient;
import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;
import se.sprinta.headhunterbackend.client.chat.dto.Choice;
import se.sprinta.headhunterbackend.client.chat.dto.Message;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.job.dto.JobIdAndTitleDtoView;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountRepository;
import se.sprinta.headhunterbackend.utils.HtmlUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceMockTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ChatClient chatClient;
    @Mock
    private HtmlUtilities htmlUtilities;

    @InjectMocks
    private JobService jobService;

    private final List<Job> jobs = new ArrayList<>();
    private final List<JobDtoView> jobDtos = new ArrayList<>();

    private final List<JobIdAndTitleDtoView> jobIdAndTitleDtoViews = new ArrayList<>();

    @Test
    @DisplayName("findAllJobs - List<Job>")
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
    @DisplayName("getAllJobDtos - List<JobDtoView>")
    void testGetAllJobDtos() {
        // Given
        given(this.jobRepository.getAllJobDtos()).willReturn(this.jobDtos);

        // When
        List<JobDtoView> actualJobDtos = this.jobService.getAllJobDtos();

        // Then
        assertThat(actualJobDtos.size()).isEqualTo(this.jobDtos.size());

        // Verify
        then(this.jobRepository).should().getAllJobDtos();
    }

    @Test
    @DisplayName("getAllJobDtosByEmail - List<Job>")
    void test_GetAllJobsByEmail() {
        Account account = new Account();
        account.setEmail("user@hh.se");
        account.setPassword("a");
        account.setRoles("user");

        // Given
        given(this.accountRepository.findById("user@hh.se")).willReturn(Optional.of(account));
        given(this.jobRepository.getAllJobDtosByUserEmail("user@hh.se")).willReturn(this.jobDtos);

        // When
        List<JobDtoView> foundJobs = this.jobService.getAllJobDtosByUserEmail("user@hh.se");

        // Then
        assertEquals(foundJobs.size(), this.jobs.size());

        // Verify
        then(this.jobRepository).should().getAllJobDtosByUserEmail("user@hh.se");
    }


    @Test
    @DisplayName("getJobsDtoIdAndTitlesByEmail - List<JobDtoView>")
    void test_GetJobIdAndTitlesDtosByEmailSuccess() {

        // Given
        given(this.jobRepository.getAllJobIdAndTitlesDtosByEmail("user@hh.se")).willReturn(this.jobIdAndTitleDtoViews);

        // When
        List<JobIdAndTitleDtoView> jobIdAndTitleDtoView = this.jobService.getAllJobIdAndTitlesDtosByEmail("user@hh.se");

        // Then
        assertEquals(this.jobIdAndTitleDtoViews.size(), jobIdAndTitleDtoView.size());

        // Verify
        then(this.jobRepository).should().getAllJobIdAndTitlesDtosByEmail("user@hh.se");
    }

    @Test
    @DisplayName("findByID - Success")
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

    @Test
    @DisplayName("findByID - Invalid Id")
    void test_FindJobByInvalidId() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.findById(Long.MAX_VALUE));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

        then(this.jobRepository).should().findById(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("getFullJobDtoByJobId - Success")
    void test_GetFullJobDtoByJobIdSuccess() {
        JobDtoView jobDtoView = new JobDtoView(
                "Title",
                "Description",
                "Recruiter Name",
                "adCompany",
                "adEmail",
                "adPhone",
                "applicationDeadline"
        );

        // Given
        given(this.jobRepository.getFullJobDtoByJobId(Mockito.anyLong())).willReturn(Optional.of(jobDtoView));

        // When
        JobDtoView foundJobDtoView = this.jobService.getFullJobDtoByJobId(Mockito.anyLong());

        // Then
        assertEquals(foundJobDtoView, jobDtoView);

        // Verify
        then(this.jobRepository).should().getFullJobDtoByJobId(Mockito.anyLong());
    }

    @Test
    @DisplayName("getFullJobDtoByJobId - Invalid Id")
    void test_GetFullJobDtoByJobIdInvalidId() {

        // Given
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.getFullJobDtoByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

        // Verify
        then(this.jobRepository).should().getFullJobDtoByJobId(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("save - Success")
    void test_Save() {
        Job job = new Job();

        // Given
        given(this.jobRepository.save(job)).willReturn(job);

        // When
        Job savedJob = this.jobRepository.save(job);

        // Then
        assertEquals(savedJob, job);

        // Verify
        then(this.jobRepository).should().save(job);
    }

    @Test
    @DisplayName("addJob - Success")
    void test_AddJob() {

        Job newJob = new Job(
                "title",
                "description",
                "instruction"
        );

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
    @DisplayName("update - Success")
    void test_UpdateSuccess() {
        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "updated title",
                "updated description",
                "updated instruction",
                "recruiterName",
                "adCompany",
                "adEmail",
                "adPhone",
                "applicationDeadline"
        );

        ArgumentCaptor<Job> jobArgumentCaptor = forClass(Job.class);

        Job foundJob = new Job();

        given(this.jobRepository.findById(1L)).willReturn(Optional.of(foundJob));
        given(this.jobRepository
                .save(jobArgumentCaptor.capture()))
                .willAnswer(invocation -> invocation.getArgument(0));

        Job savedJob = this.jobService.update(1L, jobDtoFormUpdate);
        Job capturedJob = jobArgumentCaptor.getValue();

        assertEquals(savedJob.getTitle(), capturedJob.getTitle());
        assertEquals(savedJob.getDescription(), capturedJob.getDescription());
        assertEquals(savedJob.getInstruction(), capturedJob.getInstruction());
        assertEquals(savedJob.getRecruiterName(), capturedJob.getRecruiterName());
        assertEquals(savedJob.getAdCompany(), capturedJob.getAdCompany());
        assertEquals(savedJob.getAdEmail(), capturedJob.getAdEmail());
        assertEquals(savedJob.getAdPhone(), capturedJob.getAdPhone());
        assertEquals(savedJob.getApplicationDeadline(), capturedJob.getApplicationDeadline());

        then(this.jobRepository).should().findById(1L);
        then(this.jobRepository).should().save(jobArgumentCaptor.capture());
    }

    @Test
    @DisplayName("update - Invalid Id")
    void test_UpdateInvalidId() {
        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "updated title",
                "updated description",
                "updated instruction",
                "recruiterName",
                "adCompany",
                "adEmail",
                "adPhone",
                "applicationDeadline"
        );

        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.update(Long.MAX_VALUE, jobDtoFormUpdate));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("update - Null Update")
    void test_UpdateNullUpdate() {
        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.jobService.update(1L, null));

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Update can't be null");
    }

    @Test
    @DisplayName("delete - Success")
    void test_Delete() {
        Job job = new Job();
        Account account = new Account();
        account.setEmail("email");
        job.setAccount(account);

        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(job));
        given(this.accountRepository.findAccountByEmail("email")).willReturn(Optional.of(account));
        willDoNothing().given(this.jobRepository).delete(job);

        // When
        this.jobService.delete("email", 1L);

        // Verify
        then(this.jobRepository).should().findById(1L);
        then(this.accountRepository).should().findAccountByEmail("email");
    }

    @Test
    @DisplayName("delete - Invalid User Email")
    void test_DeleteWithInvalidUserEmail() {

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.delete("Invalid Email", 1L));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("delete - Invalid Id")
    void test_DeleteWithInvalidId() {
        Account account = new Account();

        // Given
        given(this.accountRepository.findAccountByEmail("email")).willReturn(Optional.of(account));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.delete("email", Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("generate - Success")
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

        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(job));
        given(this.jobRepository.save(job)).willReturn(job);
        given(this.chatClient.generate(chatRequest)).willReturn(chatResponse);
        given(this.htmlUtilities.makeHtmlResponseSubstring(response)).willReturn(response);

        // When
        String substringResponse = this.jobService.generate(job.getId());

        // Then
        ArgumentCaptor<ChatRequest> chatRequestArgumentCaptor = ArgumentCaptor.forClass(ChatRequest.class);
        ArgumentCaptor<Job> jobArgumentCaptor = ArgumentCaptor.forClass(Job.class);

        // Verify
        then(this.jobRepository).should().findById(job.getId());
        verify(this.chatClient).generate(chatRequestArgumentCaptor.capture());
        then(this.jobRepository).should().save(jobArgumentCaptor.capture());

        // Assert
        ChatRequest capturedChatRequest = chatRequestArgumentCaptor.getValue();
        List<Message> capturedMessages = capturedChatRequest.messages();
        assertEquals(2, capturedMessages.size());
        assertEquals("system", capturedMessages.get(0).role());
        assertEquals("instruction", capturedMessages.get(0).content());
        assertEquals("user", capturedMessages.get(1).role());
        assertEquals("description", capturedMessages.get(1).content());

        Job capturedJob = jobArgumentCaptor.getValue();
        assertNotNull(capturedJob);
        assertEquals(1, capturedJob.getAds().size());

        Ad capturedAd = capturedJob.getAds().get(0);
        assertEquals("<!DOCTYPE html><html><body>generate content</body></html>", capturedAd.getHtmlCode());
        assertEquals("<!DOCTYPE html><html><body>generate content</body></html>", substringResponse);
    }

}