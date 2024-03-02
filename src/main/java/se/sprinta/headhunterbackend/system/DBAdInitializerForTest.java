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

import java.util.List;

@Component
@Transactional
@Profile("ad-test")
public class DBAdInitializerForTest implements CommandLineRunner {

    private final AdRepository adRepository;

    private final AdService adService;
    private final JobRepository jobRepository;
    private final JobService jobService;
    private final UserRepository userRepository;
    private final UserService userService;

    private List<Job> jobs;
    private List<Ad> ads;

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

        Job job1 = new Job();
        job1.setTitle("Test");
        job1.setDescription("Description");
        job1.setInstruction("Instruction");
        job1.setUser(user1);

        this.jobs.add(job1);
        user1.setJobs(jobs);
        this.userRepository.save(user1);

        Ad ad1 = new Ad();
        ad1.setHtmlCode("htmlCode");
        ad1.setJob(job1);

        this.ads.add(ad1);
        job1.setAds(ads);
        this.jobRepository.save(job1);
        this.adRepository.save(ad1);

    }
}
