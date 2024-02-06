package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

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
    void testFindAllSuccess() {
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
    void testFindByIdSuccess() {
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
    void testFindByIWithNonExistentId() {
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
    void testSaveSuccess() {
        Job newJob = new Job();
        newJob.setId(1L);
        newJob.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");

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
    void testUpdateSuccess() {
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
    void testUpdateWithNonExistentId() {
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
    void testDeleteSuccess() {
        Job j1 = new Job();
        j1.setId(1L);
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");

        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(j1));
        doNothing().when(this.jobRepository).delete(j1);

        // When
        this.jobService.delete(1L);

        // Then
        verify(this.jobRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteNonExistentId() {
        // Given
        given(this.jobRepository.findById(10L)).willThrow(new ObjectNotFoundException("job", 10L));

        // When
        Throwable thrown = catchThrowable(() -> {
            this.jobService.delete(10L);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id 10");

    }
}




















