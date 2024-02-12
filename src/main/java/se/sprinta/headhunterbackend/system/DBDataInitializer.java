package se.sprinta.headhunterbackend.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final JobService jobService;

    private final JdbcTemplate jdbcTemplate;

    public DBDataInitializer(UserService userService, JobService jobService, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jobService = jobService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        jdbcTemplate.execute("TRUNCATE TABLE jobs"); // Adjust the table name as per your schema

        /*
         * Users
         */

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

        /*
         * Jobs
         */

        JobDtoFormAdd userMikael = new JobDtoFormAdd("m@e.se", "Testare till Tesla");
        JobDtoFormAdd userAnders = new JobDtoFormAdd("a@l.se", "Pilot till GPT");

        this.jobService.addJob(userMikael);
        this.jobService.addJob(userAnders);

    }
}
