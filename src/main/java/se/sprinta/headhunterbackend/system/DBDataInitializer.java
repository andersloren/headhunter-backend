package se.sprinta.headhunterbackend.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DBDataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        User u1 = new User();
        u1.setId("d66a3164-0a9d-4efb-943b-de64057aab14");
        u1.setUsername("Mikael");
        u1.setPassword("123456");
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setId("d66a3164-0a9d-4efb-943b-de64057aab15");
        u2.setUsername("Anders");
        u2.setPassword("654321");
        u2.setRoles("user");

        this.userService.save(u1);
        this.userService.save(u1);
    }
}
