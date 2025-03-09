package se.sprinta.headhunterbackend.account;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.TestsDatabaseInitializer;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.exception.EmailAlreadyExistsException;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestsDatabaseInitializer h2DbInit;

    private List<Account> accounts = new ArrayList<>();

    private List<AccountDtoView> accountDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.h2DbInit.initializeH2Database();
        this.accounts = TestsDatabaseInitializer.getAccounts();
        this.accountDtos = this.h2DbInit.initializeH2AccountDtos();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearH2Database();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_databaseInitializer() {
        assertEquals(this.accounts.size(), 4);
        assertEquals(this.accountDtos.size(), 4);
    }

    @Test
    @DisplayName("GET - findAll - Success")
    void test_FindAll_Success() {
        List<Account> foundAccounts = this.accountService.findAll();

        assertEquals(foundAccounts.size(), this.accounts.size());

        for (int i = 0; i < foundAccounts.size(); i++) {
            assertEquals(foundAccounts.get(i).getEmail(), this.accounts.get(i).getEmail());
            assertEquals(foundAccounts.get(i).getRoles(), this.accounts.get(i).getRoles());
            assertEquals(foundAccounts.get(i).getNumber_of_jobs(), this.accounts.get(i).getNumber_of_jobs());
            assertNotNull(foundAccounts.get(i).getPassword());
            assertEquals(foundAccounts.get(i).getEmail(), this.accounts.get(i).getEmail());
        }
    }

    @Test
    @DisplayName("GET - getAccountDtos - Success")
    void test_GetAccountDtos_Success() {
        List<AccountDtoView> foundAccountDtos = this.accountService.getAccountDtos();

        System.out.println("Found Account Dtos: " + foundAccountDtos.size());
        System.out.println("Initialized Account Dtos: " + this.accountDtos.size());
        assertEquals(foundAccountDtos.size(), this.accountDtos.size());

        for (int i = 0; i < foundAccountDtos.size(); i++) {
            assertEquals(foundAccountDtos.get(i).email(), this.accountDtos.get(i).email());
            assertEquals(foundAccountDtos.get(i).roles(), this.accountDtos.get(i).roles());
            assertEquals(foundAccountDtos.get(i).number_of_jobs(), this.accountDtos.get(i).number_of_jobs());
            assertEquals(foundAccountDtos.get(i).email(), this.accountDtos.get(i).email());
        }
    }

    @Test
    @DisplayName("GET - findById - Success")
    void test_FindById_Success() {
        Account foundAccount = this.accountService.findById("user1-test@hh.se");
        assertEquals(foundAccount.getEmail(), this.accounts.get(0).getEmail());
        assertEquals(foundAccount.getRoles(), this.accounts.get(0).getRoles());
        assertEquals(foundAccount.getNumber_of_jobs(), this.accounts.get(0).getNumber_of_jobs());
        assertNotNull(foundAccount.getPassword());
    }

    @Test
    @DisplayName("GET - findById - Invalid Input - Exception")
    void test_FindById_InvalidInput_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.findById("Invalid Email"));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - Free Email - Success")
    void test_ValidateEmailAvailable_NonExistingEmail_ReturnsTrue_Success() {
        boolean isEmailNotAvailableInDatabase = this.accountService.validateEmailAvailable("availableEmail@hh.se");

        assertTrue(isEmailNotAvailableInDatabase);
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - Not Free Email - Exception")
    void test_ValidateEmailAvailable_ExistingEmail_Exception() {
        Throwable thrown = assertThrows(EmailNotFreeException.class,
                () -> this.accountService.validateEmailAvailable("user1-test@hh.se"));

        assertThat(thrown)
                .isInstanceOf(EmailNotFreeException.class)
                .hasMessage("user1-test@hh.se is already registered");

    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - Success")
    void test_GetAccountDtoByEmail_Success() {
        AccountDtoView foundAccount = this.accountService.getAccountDtoByEmail("user1-test@hh.se");

        assertEquals(foundAccount.email(), "user1-test@hh.se");
        assertEquals(foundAccount.roles(), "user");
        assertEquals(foundAccount.number_of_jobs(), 2);
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - Invalid Input - Exception")
    void test_GetAccountDtoByEmail_InvaildInput_Exception() {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.getAccountDtoByEmail("Invalid Email"));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("POST - register - Success")
    void test_Register_Success() throws IOException, URISyntaxException {
        AccountDtoFormRegister accountDtoFormRegister = new AccountDtoFormRegister(
                "registerAccount@hh.se",
                "password",
                "user");

        Account savedAccount = this.accountService.register(accountDtoFormRegister);
        assertEquals(savedAccount.getEmail(), "registerAccount@hh.se");
        assertNotNull(savedAccount.getPassword());
        assertEquals(savedAccount.getRoles(), "user");
        assertEquals(savedAccount.getNumber_of_jobs(), 0);
    }

    @Test
    @DisplayName("POST - register - Null Input - Exception")
    void test_Register_NullInput_Exception() {
        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.accountService.register(null));

        assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("Account object cannot be null");
    }

    @Test
    @DisplayName("POST - register - Email Already Registered - Exception")
    void test_Register_EmailAlreadyRegistered_Exception() {
        // Setup
        AccountDtoFormRegister accountDtoFormRegister = new AccountDtoFormRegister(
                "user1-test@hh.se",
                "a",
                "user");

        // When
        Throwable thrown = assertThrows(EmailAlreadyExistsException.class,
                () -> this.accountService.register(accountDtoFormRegister));

        // Then
        assertThat(thrown)
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email is already registered");
    }

    @Test
    @DisplayName("PUT - update - Success")
    void test_Update_Success() {
        AccountUpdateDtoForm accountUpdateDtoForm = new AccountUpdateDtoForm("newRole");

        Account updatedAccount = this.accountService.update("user1-test@hh.se", accountUpdateDtoForm);
        assertEquals(updatedAccount.getEmail(), "user1-test@hh.se");
        assertEquals(updatedAccount.getRoles(), "newRole");
        assertEquals(updatedAccount.getNumber_of_jobs(), 2);
        assertNotNull(updatedAccount.getPassword());
    }

    @Test
    @DisplayName("PUT - update - Invalid Input - Exception")
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
    @DisplayName("DELETE - delete - Invalid Input - Exception")
    void test_Delete_InvalidInput_Exception() {
        String invalidEmail = "Invalid Email";
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.delete(invalidEmail));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }
}
