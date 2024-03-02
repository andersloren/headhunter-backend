package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {

    @Mock
    AdRepository adRepository;
    @InjectMocks
    AdService adService;
    List<Ad> ads = new ArrayList<>();


    @Test
    void testFindAllAdsSuccess() {
        Ad ad1 = new Ad("htmlCode ad1");
        Ad ad2 = new Ad("htmlCode ad2");

        this.ads.add(ad1);
        this.ads.add(ad2);

        // Given
        given(this.adRepository.findAll()).willReturn(this.ads);

        // When
        List<Ad> foundAds = this.adService.findAllAds();

        // Then
        assertThat(foundAds).hasSameElementsAs(ads);

        // Verify
        then(this.adRepository).should(times(1)).findAll();
    }

    @Test
    void testFindAdByIdSuccess() {
        Ad ad1 = new Ad("abc", "htmlCode");

        // Given
        given(this.adRepository.findById("abc")).willReturn(Optional.of(ad1));

        // When
        Ad foundAd = this.adService.findById("abc");

        // Then
        assertThat(foundAd).isEqualTo(ad1);

        // Verify
        then(this.adRepository).should().findById("abc");
    }

    @Test
    void testFindAdByIdNonExistentId() {
        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                // first variation
                () -> adService.findById("non-existent-id"));
        /*      // second variation, doesn't test business logic
               () -> {
                    throw new ObjectNotFoundException("ad", "non-existent-id");
        );}*/

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id non-existent-id");
    }

    @Test
    void testFindAdsByJobIdSuccess() {
        User user = new User("m@e.se", "Mikael", "admin user", null);
        Job job = new Job(1L, "Title", "Description", user, "Instruction", "htmlCode", null, 0);

        Ad ad1 = new Ad("htmlCode 1");
        Ad ad2 = new Ad("htmlCode 2");

        this.ads.add(ad1);
        this.ads.add(ad2);

        job.setAds(ads);
        ad1.setJob(job);
        ad2.setJob(job);

        // Given
        given(this.adRepository.findByJob_Id(1L)).willReturn(ads);

        // When
        List<Ad> foundAds = this.adService.findAllAds();

        // Then
        assertThat(foundAds).hasSameElementsAs(ads);

        // Verify
        then(this.adService).should().findAdsByJobId(1L);

    }
}



