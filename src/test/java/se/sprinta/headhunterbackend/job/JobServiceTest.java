package se.sprinta.headhunterbackend.job;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.sprinta.headhunterbackend.client.chat.ChatClient;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormRemove;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserRepository;
import se.sprinta.headhunterbackend.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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


    @BeforeEach
    void setUp() {
        Job j1 = new Job();
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        Job j2 = new Job();
        j2.setDescription(".Net-junior till vårt nya kontor.");
        Job j3 = new Job();
        j3.setDescription("HR-ninja till vår nya avdelning på Mynttorget.");
        this.jobs = new ArrayList<>();
        this.jobs.add(j1);
        this.jobs.add(j2);
        this.jobs.add(j3);
    }

    @Test
    void testFindAllJobsSuccess() {
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
        j1.setId(1L);
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(j1));
        // When
        Job foundJob = this.jobService.findById(1L);
        // Then
        assertThat(foundJob.getId()).isEqualTo(j1.getId());
        assertThat(foundJob.getDescription()).isEqualTo(j1.getDescription());
        // Verify
        verify(this.jobRepository, times(1)).findById(1L);

    }

    @Test
    void testFindJobByIWithNonExistentId() {
        // Given
        given(this.jobRepository.findById(10L)).willThrow(new ObjectNotFoundException("job", 10L));

        // When
        Throwable thrown = catchThrowable(() -> {
            Job foundJob = this.jobService.findById(10L);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id 10");
    }

    @Test
    void testSaveJobSuccess() {
        Job newJob = new Job();
        User newUser = new User();
        newJob.setId(1L);
        newJob.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        newJob.setUser(null);

        // Given
        given(this.jobRepository.save(newJob)).willReturn(newJob);

        // When
        Job savedJob = this.jobService.save(newJob);

        // Then
        assertThat(savedJob.getId()).isEqualTo(newJob.getId());
        assertThat(savedJob.getDescription()).isEqualTo(newJob.getDescription());

        // Verify
        verify(this.jobRepository, times(1)).save(newJob);
    }

    @Test
    void testUpdateJobSuccess() {
        Job j1 = new Job();
        j1.setId(1L);
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");

        Job update = new Job();
        update.setDescription("Utvecklare i C++ till Volvo Cars");

        // Given)
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(j1));
        given(this.jobRepository.save(j1)).willReturn(j1);

        // When
        Job updatedJob = this.jobService.update(1L, update);

        // Then
        assertThat(updatedJob.getDescription()).isEqualTo(update.getDescription());

        // Verify
        verify(this.jobRepository, times(1)).findById(1L);
        verify(this.jobRepository, times(1)).save(j1);
    }

    @Test
    void testUpdateJobWithNonExistentId() {
        Job nonExistentJob = new Job();
        nonExistentJob.setDescription("Job that is not in db.");

        // Given
        given(this.jobRepository.findById(10L)).willThrow(new ObjectNotFoundException("job", 10L));

        // When
        Throwable thrown = catchThrowable(() -> {
            Job foundJob = this.jobService.update(10L, nonExistentJob);
        });

        // When
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id 10");
    }

    @Test
    void testDeleteJobSuccess() {
        Job j1 = new Job();
        j1.setId(1L);
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");

        int size = this.jobs.size();
        JobDtoFormRemove jobDtoFormRemove = new JobDtoFormRemove("m@e.se", 1L);
        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(j1));
        doNothing().when(this.jobRepository).delete(j1);

        // When
        this.jobService.delete(new JobDtoFormRemove("m@e.se", 1L));

        // Then
        assertThat(this.jobRepository.findAll().size()).isEqualTo(0);
        verify(this.jobRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteJobWithNonExistentId() {
        // Given
        given(this.jobRepository.findById(10L)).willThrow(new ObjectNotFoundException("job", 10L));

        // When
        Throwable thrown = catchThrowable(() -> {
            this.jobService.delete(new JobDtoFormRemove("m@e.se", 1L));
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id 10");

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
}




















