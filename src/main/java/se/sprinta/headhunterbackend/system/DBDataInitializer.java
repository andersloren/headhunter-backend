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
    }
}