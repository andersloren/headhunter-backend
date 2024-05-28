package se.sprinta.headhunterbackend.system;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

/**
 * Database entries (User objects) for demoing and debugging purposes.
 */

@Component
@Transactional
//@Profile("passive")
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DBDataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {

        // TODO: 14/03/2024 Fix these duplicates
        User admin1 = new User();
        admin1.setEmail("admin@hh.se");
        admin1.setUsername("admin");
        admin1.setPassword("a");
        admin1.setRoles("admin");

        User user1 = new User();
        user1.setEmail("user@hh.se");
        user1.setUsername("user");
        user1.setPassword("a");
        user1.setRoles("user");

        this.userService.save(admin1);
        this.userService.save(user1);
    }
}