package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.job.Job;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryH2Test {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        Account admin = new Account();
        admin.setEmail("admin-h2@hh.se");
        admin.setPassword("a");
        admin.setRoles("admin");

        Account user1 = new Account();
        user1.setEmail("user1-h2@hh.se");
        user1.setPassword("a");
        user1.setRoles("user");

        Account user2 = new Account();
        user2.setEmail("user2-h2@hh.se");
        user2.setPassword("a");
        user2.setRoles("user");

        Account user3 = new Account();
        user3.setEmail("user3-h2@hh.se");
        user3.setPassword("a");
        user3.setRoles("user");

        this.accountRepository.save(admin);
        this.accountRepository.save(user1);
        this.accountRepository.save(user2);
        this.accountRepository.save(user3);
    }

    @Test
    void test_findAccountByEmail() {
        Optional<Account> foundAccount = this.accountRepository.findAccountByEmail("admin-h2@hh.se");
        if (foundAccount.isPresent()) {
            assertEquals(foundAccount.get().getEmail(), "admin-h2@hh.se");
            assertEquals(foundAccount.get().getPassword(), "a");
            assertEquals(foundAccount.get().getRoles(), "admin");
            assertEquals(foundAccount.get().getNumber_of_jobs(), 0);
        }
    }

    @Test
    void test_GetAccountDtoByEmail_Success() {

        Optional<AccountDtoView> foundAccountDtoView = this.accountRepository.getAccountDtoByEmail("admin-h2@hh.se");
        if (foundAccountDtoView.isPresent()) {
            assertEquals(foundAccountDtoView.get().email(), "admin-h2@hh.se");
            assertEquals(foundAccountDtoView.get().roles(), "admin");
        }
    }

    @Test
    @DisplayName("validateEmailAvailable - Email Exist")
    void test_ValidateEmailAvailable_Success() {
        boolean numberOfEmailInputInInDatabase = this.accountRepository.validateEmailAvailable("admin-h2@hh.se");
        assertFalse(numberOfEmailInputInInDatabase);
    }

    @Test
    @DisplayName("validateEmailAvailable - Email Does not Exist")
    void test_validateEmailAvailable_InvalidInput() {
        boolean numberOfEmailInputInInDatabase = this.accountRepository.validateEmailAvailable("availableEmail@hh.se");
        assertTrue(numberOfEmailInputInInDatabase);
    }
}