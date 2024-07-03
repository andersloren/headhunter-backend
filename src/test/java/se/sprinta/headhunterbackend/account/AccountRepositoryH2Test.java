package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@SpringBootTest
@ActiveProfiles("test-h2")
class AccountRepositoryH2Test {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private H2DatabaseInitializer h2DbInit;

    private List<Account> accounts = new ArrayList<>();
    private List<AccountDtoView> accountDtos = new ArrayList<>();


    @BeforeEach
    void setUp() {
        this.h2DbInit.initializeH2Database();
        this.accounts = H2DatabaseInitializer.getAccounts();
        this.accountDtos = this.h2DbInit.initializeAccountDtos();
    }

    @Test
    void test_findAccountByEmail() {
        System.out.println(this.accounts.size());

        Optional<Account> foundAccount = this.accountRepository.findAccountByEmail(this.accounts.get(0).getEmail());
        if (foundAccount.isPresent()) {
            assertEquals(foundAccount.get().getEmail(), this.accounts.get(0).getEmail());
            assertNotNull(foundAccount.get().getPassword());
            assertEquals(foundAccount.get().getRoles(), this.accounts.get(0).getRoles());
            assertEquals(foundAccount.get().getNumber_of_jobs(), this.accounts.get(0).getNumber_of_jobs());
        }
    }

    @Test
    void test_GetAccountDtoByEmail_Success() {

        Optional<AccountDtoView> foundAccountDtoView = this.accountRepository.getAccountDtoByEmail(this.accounts.get(0).getEmail());
        if (foundAccountDtoView.isPresent()) {
            assertEquals(foundAccountDtoView.get().email(), this.accountDtos.get(0).email());
            assertEquals(foundAccountDtoView.get().roles(), this.accountDtos.get(0).roles());
            assertEquals(foundAccountDtoView.get().number_of_jobs(), this.accountDtos.get(0).number_of_jobs());
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