package se.sprinta.headhunterbackend.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testInitializeUserRoles() {
        User user = new User();
        user.setRoles("someRoles");

        System.out.println(user.getRoles());

        assertNotNull(user);
    }

    @Test
    void testInitializeUserEmailPasswordRolesNumberOfJobs() {
        User user = new User();
        user.setEmail(""); // TODO: 14/06/2024 the @IsEmpty annotation should not allow for this
        user.setPassword("123456");
        user.setRoles("someRole");
        user.setNumberOfJobs();

        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user.getRoles());
        System.out.println(user.getNumberOfJobs());

        assertNotNull(user);
    }

    @Test
    void testInitializeUserEmailRoles() {
        User user = new User();
        user.setEmail("user@hh.se");
        user.setRoles("newRole");

        System.out.println(user.getEmail());
        System.out.println(user.getRoles());

        assertNotNull(user);
    }
}
