/*
package se.sprinta.headhunterbackend.system;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.AdRepository;
import se.sprinta.headhunterbackend.ad.AdService;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserRepository;
import se.sprinta.headhunterbackend.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
//@Profile("ad-test")
@Transactional
public class DBAdInitializerForTest implements CommandLineRunner {

    private final AdRepository adRepository;
    private final AdService adService;
    private final JobRepository jobRepository;
    private final JobService jobService;
    private final UserRepository userRepository;
    private final UserService userService;

    private List<Job> jobs = new ArrayList<>();
    private List<Ad> ads = new ArrayList<>();

    public DBAdInitializerForTest(AdRepository adRepository, AdService adService, JobRepository jobRepository, JobService jobService, UserRepository userRepository, UserService userService, List<Job> jobs, List<Ad> ads) {
        this.adRepository = adRepository;
        this.adService = adService;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jobs = jobs;
        this.ads = ads;
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setUsername("Mikael");
        user1.setPassword("a");
        user1.setRoles("admin user");

        Job job1 = this.jobService.addJob(new JobDtoFormAdd("m@e.se", "job1 Title", "job1 Description", "job1 Instruction"));

        user1.addJob(job1);

        Ad ad1 = new Ad("htmlCode 1");
        Ad ad2 = new Ad("htmlCode 2");
        Ad ad3 = new Ad("htmlCode 3");
        Ad ad4 = new Ad("htmlCode 4");
        Ad ad5 = new Ad("htmlCode 5");

        this.userService.save(user1);

        ad1.setJob(job1);
        ad2.setJob(job1);
        ad3.setJob(job1);
        ad4.setJob(job1);
        ad5.setJob(job1);

        ad1.setId("abc");
        ad1.setId("abc1");
        ad1.setId("abc2");
        ad1.setId("abc3");
        ad1.setId("abc4");

        this.ads.add(ad1);
        this.ads.add(ad2);
        this.ads.add(ad3);
        this.ads.add(ad4);
        this.ads.add(ad5);

        job1.setAds(ads);
        this.jobService.save(job1);

        this.adRepository.save(ad1);
        this.adRepository.save(ad2);
        this.adRepository.save(ad3);
        this.adRepository.save(ad4);
        this.adRepository.save(ad5);


    }
}
*/
