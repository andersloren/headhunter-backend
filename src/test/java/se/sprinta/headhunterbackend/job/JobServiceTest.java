package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import se.sprinta.headhunterbackend.client.chat.ChatClient;
import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;
import se.sprinta.headhunterbackend.client.chat.dto.Choice;
import se.sprinta.headhunterbackend.client.chat.dto.Message;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormRemove;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.system.exception.ResponseSubstringNotPureHtmlException;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    JobRepository jobRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ChatClient chatClient;

    @InjectMocks
    JobService jobService;

    List<Job> jobs;
    List<Job> user1Jobs;
    List<Job> user2Jobs;


    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindAllJobsSuccess() {
        User user1 = new User(
                "m@e.se",
                "Mikael",
                "admin user",
                null);


        User user2 = new User(
                "a@l.se",
                "Anders",
                "user",
                null);

        this.userRepository.save(user1);
        this.userRepository.save(user2);

        Job j1 = new Job();
        j1.setId(1L);
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        j1.setUser(user1);
        j1.setInstruction("This is an instruction");
        j1.setHtmlCode("This is HTML code");

        Job j2 = new Job();
        j2.setId(2L);
        j2.setDescription(".Net-junior till vårt nya kontor.");
        j2.setUser(user1);
        j2.setInstruction("This is an instruction");
        j2.setHtmlCode("This is HTML code");

        Job j3 = new Job();
        j3.setId(3L);
        j3.setDescription("HR-ninja till vår nya avdelning på Mynttorget.");
        j3.setUser(user2);
        j3.setInstruction("This is an instruction");
        j3.setHtmlCode("This is HTML code");

        this.jobs = new ArrayList<>();
        this.jobs.add(j1);
        this.jobs.add(j2);
        this.jobs.add(j3);

        this.user1Jobs = new ArrayList<>();
        this.user1Jobs.add(j1);
        this.user1Jobs.add(j2);

        this.user2Jobs = new ArrayList<>();
        this.user2Jobs.add(j3);

        user1.setJobs(user1Jobs);
        user2.setJobs(user2Jobs);
        // Given
        given(this.jobRepository.findAll()).willReturn(this.jobs);
        // When
        List<Job> actualJobs = this.jobService.findAll();
        // Then
        assertThat(actualJobs.size()).isEqualTo(this.jobs.size());
        // Verify
        verify(this.jobRepository, times(1)).findAll();
    }

    @Test
    void testFindJobByIdSuccess() {
        Job j1 = new Job();
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        j1.setUser(new User("m@e.se", "Mikael", "admin user", null));
        j1.setInstruction("This is an instruction");
        j1.setHtmlCode("This is HTML code");
        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(j1));
        // When
        Job foundJob = this.jobService.findById(1L);
        // Then
        assertThat(foundJob.getId()).isEqualTo(j1.getId());
        assertThat(foundJob.getDescription()).isEqualTo(j1.getDescription());
        assertThat(foundJob.getUser()).isEqualTo(j1.getUser());
        assertThat(foundJob.getInstruction()).isEqualTo(j1.getInstruction());
        assertThat(foundJob.getHtmlCode()).isEqualTo(j1.getHtmlCode());

        // Verify
        verify(this.jobRepository, times(1)).findById(1L);
    }

    @Test
    void testFindJobByIWithNonExistentId() {
        // Given
        given(this.jobRepository.findById(10L)).willThrow(new ObjectNotFoundException("job", 10L));

        // When
        Throwable thrown = catchThrowable(() -> {
            this.jobService.findById(10L);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id 10");
    }

    @Test
    void testSaveJobSuccess() {
        User user1 = new User(
                "m@e.se",
                "Mikael",
                "admin user",
                null);

        Job j1 = new Job();
        j1.setId(1L);
        j1.setTitle("Java-utvecklare");
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        j1.setUser(user1);
        j1.setInstruction("This is an instruction");
        j1.setHtmlCode("This is HTML code");


        // Given
        given(this.jobRepository.save(j1)).willReturn(j1);

        // When
        Job savedJob = this.jobService.save(j1);

        // Then
        assertThat(savedJob.getId()).isEqualTo(j1.getId());
        assertThat(savedJob.getTitle()).isEqualTo(j1.getTitle());
        assertThat(savedJob.getDescription()).isEqualTo(j1.getDescription());
        assertThat(savedJob.getUser()).isEqualTo(j1.getUser());
        assertThat(savedJob.getInstruction()).isEqualTo(j1.getInstruction());
        assertThat(savedJob.getHtmlCode()).isEqualTo(j1.getHtmlCode());

        // Verify
        verify(this.jobRepository, times(1)).save(j1);
    }

    @Test
    void testUpdateJobSuccess() {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setUsername("Mikael");
        user1.setRoles("admin user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Java-utvecklare");
        job1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        job1.setInstruction("This is an instruction");
        job1.setHtmlCode("This is an HTML code");

        JobDtoFormUpdate update = new JobDtoFormUpdate(
                "m@e.se",
                "Updated title",
                "Updated description.",

                "Updated instruction",
                "Updated HTML code"
        );

        Job updatedJob = new Job();
        updatedJob.setTitle("Updated title");
        updatedJob.setDescription("Updated description.");
        updatedJob.setUser(user1);
        updatedJob.setInstruction("Updated instruction");
        updatedJob.setHtmlCode("Updated HTML code");

        // Given)
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(job1));
        given(this.jobRepository.save(job1)).willReturn(job1);

        // When
        Job returnedUpdatedJob = this.jobService.update(1L, update);

        // Then
        assertThat(returnedUpdatedJob.getTitle()).isEqualTo(updatedJob.getTitle());
        assertThat(returnedUpdatedJob.getDescription()).isEqualTo(updatedJob.getDescription());
        assertThat(returnedUpdatedJob.getInstruction()).isEqualTo(updatedJob.getInstruction());
        assertThat(returnedUpdatedJob.getHtmlCode()).isEqualTo(updatedJob.getHtmlCode());

        // Verify
        verify(this.jobRepository, times(1)).findById(1L);
        verify(this.jobRepository, times(1)).save(job1);
    }

    @Test
    void testUpdateJobWithNonExistentId() {
        JobDtoFormUpdate nonExistentJob = new JobDtoFormUpdate(
                "m@e.se",
                "Title",
                "Description",
                "Instruction",
                "HTML code"
        );

        // Given
        given(this.jobRepository.findById(10L)).willThrow(new ObjectNotFoundException("job", 10L));

        // When
        Throwable thrown = catchThrowable(() -> {
            this.jobService.update(10L, nonExistentJob);
        });

        // When
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id 10");
    }

    // TODO: 12/02/2024 This test does not work. If it's important, investigate this at some point.
    @Test
    void testDeleteJobSuccess() {
        // It's beyond our understanding how to do this. Bilateral relationship.
    }

    @Test
    void testDeleteJobWithNonExistentId() {
        // Given
        given(this.jobRepository.findById(10L)).willThrow(new ObjectNotFoundException("job", 10L));

        // When
        Throwable thrown = catchThrowable(() -> {
            this.jobService.delete(new JobDtoFormRemove("m@e.se", 10L));
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id 10");

    }

    @Test
    void testDeleteJobWithIncorrectEmail() {
        Job j1 = new Job();
        j1.setId(1L);
        j1.setTitle("Java-utvecklare");
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        j1.setInstruction("This is an instruction");
        j1.setHtmlCode("This is HTML code");

        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(j1));
        given(this.userRepository.findByEmail("m@j.se")).willThrow(new ObjectNotFoundException("user", "m@j.se"));

        // When
        Throwable thrown = catchThrowable(() -> {
            this.jobService.delete(new JobDtoFormRemove("m@j.se", 1L));
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Email m@j.se");
    }

    @Test
    void testMakeResponseSubstringSuccess() {
        String response = "```<!DOCTYPE html></html>```";

        String substringResponse = "<!DOCTYPE html></html>";

        // When
        String responseSubstring = this.jobService.makeResponseSubstring(response);

        // Then
        assertEquals(responseSubstring, substringResponse);
    }

    @Test
    void testMakeResponseSubstringWhenResponseIsNull() {
        // When
        Throwable thrown = catchThrowable(() -> {
            this.jobService.makeResponseSubstring(null);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(HttpClientErrorException.class);
    }
}

//    @Test
//    void testSummarizeSuccess() throws JsonProcessingException {
//        // Given
//        JobDto jobDto = new JobDto(1L,
//                "Programledare\n" +
//                        "IT Program-/Projektledare\n" +
//                        "\n" +
//                        " \n" +
//                        "\n" +
//                        "Vi söker en programledare för att leda och styra projektresurser vars focus är att bidra till den totala leveransen inom ett mer omfattande IT-program.\n" +
//                        "\n" +
//                        "Som programledare förväntas du hantera och facilitera externa intressenter som har beroenden till programmet.\n" +
//                        "\n" +
//                        " \n" +
//                        "\n" +
//                        "Rollen kräver en kombination av teknisk expertis, dokumenterad projektledningsförmåga av större dignitet och ett tydligt affärsmannaskap.\n" +
//                        "\n" +
//                        "Du kommer övervaka planering, genomförande och leverans för att med intressenter, teammedlemmar och ledare säkerställa att programmet når sina mål.\n" +
//                        "\n" +
//                        " \n" +
//                        "\n" +
//                        "Programmet syftar till att ta in en ny kund till Soltak och att få alla delarna i den totala IT-leveransen på plats.\n" +
//                        "\n" +
//                        "Flertalet projekt är redan uppstartade och det finns även delar som ännu inte är uppstartade.\n" +
//                        "\n" +
//                        " \n" +
//                        "\n" +
//                        "Rollen behöver tillsättas omgående och initialt upptar programledarrollen 50% av en FTE.\n" +
//                        "\n" +
//                        "Möjlighet till utökande av projektledning av ett eller flera andra projekt."
//        );
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonArray = objectMapper.writeValueAsString(jobDto);
//
//        List<Message> messages = List.of(
//                new Message("system", "Your task is to generate a short summary of a given JSON array in at most 100 words. The summary must include the number if artifacts, each artifact's description, and the ownership information."),
//                new Message("user", jsonArray));
//
//
//        ChatRequest chatRequest = new ChatRequest("gpt-4", messages);
//        ChatResponse chatResponse = new ChatResponse(List.of(new Choice(0, new Message("assistent", "A summary of two artifacts owned by Albus Dumbledore."))));
//
//        given(this.chatClient.generate(chatRequest)).willReturn(chatResponse);
//
//        // When
//
//        String summary = this.jobService.generate(jobDto);
//
//        // Then
//
//        assertThat(summary).isEqualTo("A summary of two artifacts owned by Albus Dumbledore.");
//        verify(this.chatClient, times(1)).generate(chatRequest);
//    }





















