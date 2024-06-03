package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.user.User;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class AdServiceIntegratedTest {

    private AdService adService;
    private JobService jobService;
    private AdRepository adRepository;
    private JobRepository jobRepository;

    @BeforeEach
    public void setUp() {
        this.jobService = Mockito.mock(JobService.class);
        this.adRepository = Mockito.mock(AdRepository.class);
        this.jobRepository = Mockito.mock(JobRepository.class);
        this.adService = new AdService(this.adRepository, this.jobService, this.jobRepository);
    }

    @Test
    void testSaveAdSuccess() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User("m@e.se", "admin user", null);
        Job job = new Job(1L, "Title", "Description", "Instruction", user, null, 0);
        Ad newAd = new Ad("abc", "htmlCode", job);
        List<Ad> ads = new ArrayList<>();
        ads.add(newAd);
        job.setAds(ads);

        // Given
        given(this.adRepository.save(newAd)).willReturn(newAd);
        given(this.jobRepository.findById(job.getId())).willReturn(Optional.of(job));

        // When
        Ad savedAd = this.adService.addAd(job.getId(), newAd);

        assertThat(savedAd.getHtmlCode()).isEqualToIgnoringCase(newAd.getHtmlCode());

        // This doesn't make any sense...
        then(this.jobService).should().save(job);
    }
}

/*        this.jobRepository = Mockito.mock(JobRepository.class);*/
