package se.sprinta.headhunterbackend.account;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test-h2")
@Transactional
public class AccountServiceH2Test {

    @Autowired
    private AccountService accountService;

    @Autowired
    private H2DatabaseInitializer dbInit;

    @BeforeEach
    void setUp() {
        this.dbInit.initializeH2Database();
    }

    @AfterEach
    void tearDown() {
        this.dbInit.clearDatabase();
    }

    @Test
    @DisplayName("findAll - Success")
    void test_FindAll_Success() {
        List<Account> actualAccounts = this.accountService.findAll();

        for (Account ac : actualAccounts) {
            System.out.println(ac.getEmail());
        }

        assertEquals(actualAccounts.size(), 4);
        assertEquals(actualAccounts.get(0).getEmail(), "admin-h2@hh.se");
        assertEquals(actualAccounts.get(0).getRoles(), "admin");
        assertEquals(actualAccounts.get(0).getNumber_of_jobs(), 0);
        assertNotNull(actualAccounts.get(0).getPassword());
        assertEquals(actualAccounts.get(1).getEmail(), "user1-h2@hh.se");
        assertEquals(actualAccounts.get(1).getRoles(), "user");
        assertEquals(actualAccounts.get(1).getNumber_of_jobs(), 2);
        assertNotNull(actualAccounts.get(1).getPassword());
        assertEquals(actualAccounts.get(2).getEmail(), "user2-h2@hh.se");
        assertEquals(actualAccounts.get(2).getRoles(), "user");
        assertEquals(actualAccounts.get(2).getNumber_of_jobs(), 1);
        assertNotNull(actualAccounts.get(2).getPassword());
        assertEquals(actualAccounts.get(3).getEmail(), "user3-h2@hh.se");
        assertEquals(actualAccounts.get(3).getRoles(), "user");
        assertEquals(actualAccounts.get(3).getNumber_of_jobs(), 1);
        assertNotNull(actualAccounts.get(3).getPassword());

    }

    @Test
    @DisplayName("findById - Success")
    void test_FindById_Success() {
        Account foundAccount = this.accountService.findById("admin-h2@hh.se");
        assertEquals(foundAccount.getEmail(), "admin-h2@hh.se");
        assertEquals(foundAccount.getRoles(), "admin");
        assertEquals(foundAccount.getNumber_of_jobs(), 0);
        assertNotNull(foundAccount.getPassword());
    }

    @Test
    @DisplayName("findById - Invalid Input - Exception")
    void test_FindById_InvalidInput_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.findById("Invalid Email"));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("validateEmailAvailable - Non-Existing Email - Success")
    void test_ValidateEmailAvailable_NonExistingEmail_ReturnsTrue_Success() {
        boolean isEmailNotAvailableInDatabase = this.accountService.validateEmailAvailable("availableEmail@hh.se");

        assertTrue(isEmailNotAvailableInDatabase);
    }

    @Test
    @DisplayName("validateEmailAvailable - Existing Email - Exception")
    void test_ValidateEmailAvailable_ExistingEmail_Exception() {
        Throwable thrown = assertThrows(EmailNotFreeException.class,
                () -> this.accountService.validateEmailAvailable("admin-h2@hh.se"));

        assertThat(thrown)
                .isInstanceOf(EmailNotFreeException.class)
                .hasMessage("admin-h2@hh.se is already registered");

    }

    @Test
    @DisplayName("getAccountDtoByEmail - Success")
    void test_GetAccountDtoByEmail_Success() {
        AccountDtoView foundAccount = this.accountService.getAccountDtoByEmail("admin-h2@hh.se");

        assertEquals(foundAccount.email(), "admin-h2@hh.se");
        assertEquals(foundAccount.roles(), "admin");
        assertEquals(foundAccount.number_of_jobs(), 0);
    }

    @Test
    @DisplayName("getAccountDtoByEmail - Invalid Input - Exception")
    void test_GetAccountDtoByEmail_InvaildInput_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.getAccountDtoByEmail("Invalid Email"));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("save - Success")
    void test_Save_Success() {
        Account newAccount = new Account();
        newAccount.setEmail("newAccount@hh.se");
        newAccount.setPassword("password");
        newAccount.setRoles("user");

        Account savedAccount = this.accountService.save(newAccount);
        assertEquals(savedAccount.getEmail(), "newAccount@hh.se");
        assertNotNull(savedAccount.getPassword());
        assertEquals(savedAccount.getRoles(), "user");
        assertEquals(savedAccount.getNumber_of_jobs(), 0);
    }

    @Test
    @DisplayName("register - Success")
    void test_Register_Success() {
        AccountDtoFormRegister accountDtoFormRegister = new AccountDtoFormRegister(
                "registerAccount@hh.se",
                "password"
        );

        Account savedAccount = this.accountService.register(accountDtoFormRegister);
        assertEquals(savedAccount.getEmail(), "registerAccount@hh.se");
        assertNotNull(savedAccount.getPassword());
        assertEquals(savedAccount.getRoles(), "user");
        assertEquals(savedAccount.getNumber_of_jobs(), 0);
    }

    @Test
    @DisplayName("Update - Success")
    void test_Update_Success() {
        AccountUpdateDtoForm accountUpdateDtoForm = new AccountUpdateDtoForm("newRole");

        Account updatedAccount = this.accountService.update("user1-h2@hh.se", accountUpdateDtoForm);
        assertEquals(updatedAccount.getEmail(), "user1-h2@hh.se");
        assertEquals(updatedAccount.getRoles(), "newRole");
        assertEquals(updatedAccount.getNumber_of_jobs(), 2);
        assertNotNull(updatedAccount.getPassword());
    }

    @Test
    @DisplayName("update - Invalid Input - Exception")
    void test_Update_InvalidInput_Exception() {
        AccountUpdateDtoForm accountUpdateDtoForm = new AccountUpdateDtoForm("newRole");

        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.update("Invalid Email", accountUpdateDtoForm));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    // Delete Test can be found in AccountServiceComplementaryH2Test

    @Test
    @DisplayName("delete - Invalid Input - Exception")
    void test_Delete_InvalidInput_Exception() {
        String invalidEmail = "Invalid Email";
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.delete(invalidEmail));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }
}
