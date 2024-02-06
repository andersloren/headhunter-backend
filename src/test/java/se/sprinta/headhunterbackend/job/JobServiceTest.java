package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void save() {
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
    void update() {
    }

    @Test
    void delete() {
    }
}