package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.*;

@ActiveProfiles("mock-test")
@ExtendWith(MockitoExtension.class)
class AccountServiceMockTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    AccountService accountService;

    List<Account> accounts = new ArrayList<>();
    List<AccountDtoView> accountDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
        this.accountDtos = MockDatabaseInitializer.initializeMockAccountDtos();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_DataInitializer() {
        System.out.println("AccountServiceMockTest, accounts size: " + this.accounts.size());
        for (Account account : this.accounts) {
            System.out.println(account.toString());
        }
        System.out.println("AccountServiceMockTest, accountDtos size: " + this.accountDtos.size());
        for (AccountDtoView accountDto : this.accountDtos) {
            System.out.println(accountDto.toString());
        }
    }

    @Test
    @DisplayName("GET - findAll - Success")
    void test_FindAll_Success() {
        // Given
        given(this.accountRepository.findAll()).willReturn(this.accounts);

        // When
        List<Account> actualAccounts = this.accountService.findAll();

        // Then
        assertThat(actualAccounts.size()).isEqualTo(this.accounts.size());
        for (int i = 0; i < actualAccounts.size(); i++) {
            if (!actualAccounts.get(i).equals(this.accounts.get(i))) {
                assertEquals(actualAccounts.get(i).getEmail(), this.accounts.get(i).getEmail());
                assertNotNull(actualAccounts.get(i).getPassword());
                assertEquals(actualAccounts.get(i).getRoles(), this.accounts.get(i).getRoles());
                assertEquals(actualAccounts.get(i).getNumber_of_jobs(), this.accounts.get(i).getNumber_of_jobs());
            }
        }

        // Verify
        then(this.accountRepository).should().findAll();
    }

    @Test
    @DisplayName("GET - getAccountDtos - Success")
    void test_GetAccountDtos_Success() {
        // Given
        given(this.accountRepository.getAccountDtos()).willReturn(this.accountDtos);

        // When
        List<AccountDtoView> foundAccountDtos = this.accountService.getAccountDtos();

        // Then
        assertEquals(foundAccountDtos.size(), this.accountDtos.size());
        for (int i = 0; i < foundAccountDtos.size(); i++) {

            assertEquals(foundAccountDtos.get(i).email(), this.accounts.get(i).getEmail());
            assertEquals(foundAccountDtos.get(i).roles(), this.accounts.get(i).getRoles());
            assertEquals(foundAccountDtos.get(i).number_of_jobs(), this.accounts.get(i).getNumber_of_jobs());
        }
    }

    @Test
    @DisplayName("GET - findById - Success")
    void test_findById_ExistingEmail_Success() {
        // Given
        given(this.accountRepository.findById("user1-mock@hh.se")).willReturn(Optional.of(accounts.get(0)));

        // When
        Account foundAccount = this.accountService.findById("user1-mock@hh.se");

        // Then
        assertEquals(foundAccount.getEmail(), accounts.get(0).getEmail());
        assertEquals(foundAccount.getRoles(), accounts.get(0).getRoles());
        assertEquals(foundAccount.getNumber_of_jobs(), accounts.get(0).getNumber_of_jobs());

        // Verify
        then(this.accountRepository).should().findById("user1-mock@hh.se");
    }

    @Test
    @DisplayName("GET - findById - Non-Existing Email - Exception")
    void test_findById_NonExistingEmail_Exception() {
        // Given
        given(this.accountRepository.findById("abc"))
                .willThrow(new se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException("account", "abc"));

        // When
        Throwable thrown = assertThrows(se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException.class,
                () -> this.accountService.findById("abc"));

        // Then
        assertThat(thrown)
                .isInstanceOf(se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - Non-Existing Email")
    void test_ValidateEmailAvailable_NonExistingEmail_ReturnsTrue_Success() {
        // Given
        given(this.accountRepository.validateEmailAvailable("availableEmail@hh.se")).willReturn(true);

        // When
        boolean isEmailNotAvailableInDatabase = this.accountService.validateEmailAvailable("availableEmail@hh.se");

        // Then
        assertTrue(isEmailNotAvailableInDatabase);
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - Existing Email - Exception")
    void test_ValidateEmailAvailable_ExistingEmail_Exception() {
        // Given
        given(this.accountRepository.validateEmailAvailable("admin@hh.se")).willReturn(false);

        Throwable thrown = assertThrows(EmailNotFreeException.class,
                () -> this.accountService.validateEmailAvailable("admin@hh.se"));

        assertThat(thrown)
                .isInstanceOf(EmailNotFreeException.class)
                .hasMessage("admin@hh.se is already registered");
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - Success")
    void test_GetAccountDtoByEmail_Success() {
        AccountDtoView accountDtoView = this.accountDtos.get(0);

        given(this.accountRepository.getAccountDtoByEmail("admin-mock@hh.se")).willReturn(Optional.of(accountDtoView));

        // When
        AccountDtoView returnedAccountDtoView = this.accountService.getAccountDtoByEmail("admin-mock@hh.se");

        // Then
        if (!returnedAccountDtoView.equals(accountDtoView)) {
            assertEquals(returnedAccountDtoView.email(), this.accountDtos.get(0).email());
            assertEquals(returnedAccountDtoView.roles(), this.accountDtos.get(0).roles());
            assertEquals(returnedAccountDtoView.number_of_jobs(), this.accountDtos.get(0).number_of_jobs());
        }

        // Verify
        then(this.accountRepository).should().getAccountDtoByEmail("admin-mock@hh.se");
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - Non-Existing Email - Exception")
    void test_GetAccountDtoByEmail_NonExistentEmail() {
        // Given
        given(this.accountRepository.getAccountDtoByEmail("abc")).willThrow(new ObjectNotFoundException("account", "abc"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.getAccountDtoByEmail("abc"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");

        // Verify
        then(this.accountRepository).should().getAccountDtoByEmail("abc");
    }

    @Test
    @DisplayName("POST - register - Success")
    void test_RegisterAccount_Success() throws IOException, URISyntaxException {
        // Setup
        AccountDtoFormRegister accountDtoFormRegister = new AccountDtoFormRegister("newUser@hh.se", "123456", "user");

        ArgumentCaptor<Account> accountArgumentCaptor = forClass(Account.class);

        // Given
        given(this.accountRepository.save(
                accountArgumentCaptor.capture()))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(this.passwordEncoder.encode(accountDtoFormRegister.password())).willReturn("Encoded Password");

        // When
        Account returnedAccount = this.accountService.register(accountDtoFormRegister);

        // Then
        Account capturedAccount = accountArgumentCaptor.getValue();

        if (!returnedAccount.equals(capturedAccount)) {
            assertEquals(returnedAccount.getEmail(), capturedAccount.getEmail());
            assertNotNull(returnedAccount.getPassword());
            assertEquals(returnedAccount.getRoles(), capturedAccount.getRoles());
            assertEquals(returnedAccount.getNumber_of_jobs(), capturedAccount.getNumber_of_jobs());
        }

        // Verify
        then(this.accountRepository).should().save(accountArgumentCaptor.capture());
    }

    @Test
    @DisplayName("POST - register - Null Object (Exception) ")
    void test_RegisterAccount_NullObject() {

        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.accountService.save(null));

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Account object cannot be null");
    }

    @Test
    @DisplayName("PUT - update - Success")
    void test_UpdateOwnAccount_Success() {
        Account existingAccount = this.accounts.get(0);

        AccountUpdateDtoForm accountUpdateDtoForm = new AccountUpdateDtoForm("admin"); // From admin user to just admin

        // Given
        given(this.accountRepository.findAccountByEmail("user1-mock@hh.se")).willReturn(Optional.of(existingAccount));
        given(this.accountRepository.save(existingAccount)).willReturn(existingAccount);

        // When
        Account updatedAccount = this.accountService.update("user1-mock@hh.se", accountUpdateDtoForm);

        // Then
        if (!updatedAccount.equals(existingAccount)) {
            assertEquals(updatedAccount.getEmail(), existingAccount.getEmail());
            assertEquals(updatedAccount.getRoles(), "admin");
            assertEquals(updatedAccount.getEmail(), existingAccount.getEmail());
            assertEquals(updatedAccount.getNumber_of_jobs(), existingAccount.getNumber_of_jobs());
        }

        // Verify
        then(this.accountRepository).should().findAccountByEmail("user1-mock@hh.se");
        then(this.accountRepository).should().save(existingAccount);
    }

    @Test
    @DisplayName("PUT - update - Non-Existing Email - Exception")
    void test_UpdateAccount_NonExistentId() {
        String email = "abc";
        AccountUpdateDtoForm update = new AccountUpdateDtoForm("newRoles");

        // Given
        given(this.accountRepository.findAccountByEmail(email)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.update("abc", update));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");

        // Verify
        then(this.accountRepository).should().findAccountByEmail("abc");
    }

    @Test
    @DisplayName("DELETE - deleteAccount - Success")
    void test_DeleteAccount_Success() {
        Account account = this.accounts.get(1);

        // Given
        given(this.accountRepository.findAccountByEmail("user1-mock@hh.se")).willReturn(Optional.of(account));
        willDoNothing().given(this.accountRepository).delete(account);

        // When
        this.accountService.delete("user1-mock@hh.se");

        // Then
        then(this.accountRepository).should().delete(account);
    }

    @Test
    @DisplayName("DELETE - deleteAccount - Non-Existing Email - Exception")
    void test_DeleteAccount_NonExistentId() {
        // Given
        given(this.accountRepository.findAccountByEmail("abc")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.delete("abc"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");

        // Verify
        then(this.accountRepository).should().findAccountByEmail("abc");
    }
}
