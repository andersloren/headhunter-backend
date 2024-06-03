package se.sprinta.headhunterbackend.ad;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DataJpaTest
public class AdRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Mock
    AdRepository adRepository;

    List<Ad> ads = new ArrayList<>();

    @BeforeEach
    void setup() {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setPassword("a");
        user1.setRoles("admin user");

        entityManager.persist(user1);

        Job job1 = new Job();
        job1.setTitle("Test");
        job1.setDescription("Description");
        job1.setInstruction("Instruction");
        job1.setUser(user1);

        entityManager.persist(job1);

        List<Job> jobs = new ArrayList<>();
        jobs.add(job1);
        user1.setJobs(jobs);

        Ad ad1 = new Ad();
        ad1.setHtmlCode("htmlCode");
        ad1.setJob(job1);

        this.ads.add(ad1);

        entityManager.persist(ad1);
        entityManager.flush();
    }

    @Test
    void testFindAdsByJobId() {
        Job job1 = new Job();
        job1.setTitle("Test");
        job1.setDescription("Description");
        job1.setInstruction("Instruction");

        Ad ad1 = new Ad();
        ad1.setHtmlCode("htmlCode");
        ad1.setJob(job1);

        Ad ad2 = new Ad();
        ad2.setHtmlCode("htmlCode");
        ad2.setJob(job1);

        this.ads.add(ad1);
        this.ads.add(ad2);

        // Given
        given(this.adRepository.findByJob_Id(1L)).willReturn(ads);

        // When
        List<Ad> foundAds = this.adRepository.findByJob_Id(1L);

        // Then
        assertThat(foundAds).hasSameElementsAs(ads);

        // Verify
        then(this.adRepository).should().findByJob_Id(1L);
    }
}
