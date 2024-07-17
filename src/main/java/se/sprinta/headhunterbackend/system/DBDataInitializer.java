package se.sprinta.headhunterbackend.system;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountService;

/**
 * Database entries (User objects) for demoing and debugging purposes.
 */

@Component
@Profile("passive")
@Transactional
public class DBDataInitializer implements CommandLineRunner {

    private final AccountService accountService;

    public DBDataInitializer(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) {

        Account admin1 = new Account();
        admin1.setEmail("admin@hh.se");
        admin1.setPassword("a");
        admin1.setRoles("admin");

        Account account1 = new Account();
        account1.setEmail("user@hh.se");
        account1.setPassword("a");
        account1.setRoles("user");

        this.accountService.save(admin1);
        this.accountService.save(account1);
    }
}