package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test-h2")
class AccountRepositoryH2Test {

    @Autowired
    private AccountRepository accountRepository;



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
}