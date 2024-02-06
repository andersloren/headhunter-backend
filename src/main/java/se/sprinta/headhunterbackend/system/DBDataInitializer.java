package se.sprinta.headhunterbackend.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final JobService jobService;

    public DBDataInitializer(UserService userService, JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }


    @Override
    public void run(String... args) {
        User u1 = new User();
        u1.setEmail("m@e.se");
        u1.setUsername("Mikael");
        u1.setPassword("a");
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setEmail("a@l.se");
        u2.setUsername("Anders");
        u2.setPassword("a");
        u2.setRoles("user");

        this.userService.save(u1);
        this.userService.save(u2);

        Job j1 = new Job();
        j1.setDescription("Testare till Tesla");
        this.jobService.save(j1);
        Job j2 = new Job();
        j2.setDescription("Pilot till GPT");
        this.jobService.save(j2);

    }
}
