package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class AdServiceIntegratedTest {

    private AdService adService;
    private JobService jobService;
    private AdRepository adRepository;

    @BeforeEach
    public void setUp() {
        jobService = Mockito.mock(JobService.class);
        adRepository = Mockito.mock(AdRepository.class);
        adService = new AdService(adRepository, jobService);
    }

    @Test
    void testSaveAdSuccess() {
        User user = new User("m@e.se", "Mikael", "admin user", null);
        Job job = new Job(1L, "Title", "Description", user, "Instruction", "htmlCode", null, 0);
        Ad ad = new Ad("abc", "htmlCode", job);
        List<Ad> ads = new ArrayList<>();
        ads.add(ad);
        job.setAds(ads);

        given(jobService.findById(job.getId())).willReturn(job);
        given(adRepository.save(any(Ad.class))).willReturn(ad);

        Ad savedAd = adService.saveAd(ad, job.getId());

        assertThat(savedAd).isEqualTo(ad);

        then(jobService).should().save(job);
        then(adRepository).should().save(ad);
    }
}

