/*
package se.sprinta.headhunterbackend.system;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
@Profile("dev")
@Transactional
public class DBJobInitializerForTest implements CommandLineRunner {

    private final UserService userService;
    private final JobService jobService;

    public DBJobInitializerForTest(UserService userService, JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setUsername("Mikael");
        user1.setPassword("a");
        user1.setRoles("admin user");

        User user2 = new User();
        user2.setEmail("a@l.se");
        user2.setUsername("Anders");
        user2.setPassword("a");
        user2.setRoles("user");

        this.userService.save(user1);
        this.userService.save(user2);

        Job job1 = this.jobService.addJob(new JobDtoFormAdd("m@e.se", "job1 Title", "job1 Description", "job1 Instruction"));
        Job job2 = this.jobService.addJob(new JobDtoFormAdd("m@e.se", "job2 Title", "job2 Description", "job2 Instruction"));
        Job job3 = this.jobService.addJob(new JobDtoFormAdd("m@e.se", "job3 Title", "job3 Description", "job3 Instruction"));
        Job job4 = this.jobService.addJob(new JobDtoFormAdd("a@l.se", "job4 Title", "job4 Description", "job4 Instruction"));
        Job job5 = this.jobService.addJob(new JobDtoFormAdd("a@l.se", "job5 Title", "job5 Description", "job5 Instruction"));

        user1.addJob(job1);
        user1.addJob(job2);
        user1.addJob(job3);
        user2.addJob(job4);
        user2.addJob(job5);
    }
}
*/
