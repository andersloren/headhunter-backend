package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.*;

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

    @AfterEach
    void tearDown() {
        this.accounts.clear();
    }

    @Test
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
    @DisplayName("validateEmailAvailable - Non-Existing Email")
    void test_ValidateEmailAvailable_NonExistingEmail_ReturnsTrue_Success() {
        // Given
        given(this.accountRepository.validateEmailAvailable("availableEmail@hh.se")).willReturn(true);

        // When
        boolean isEmailNotAvailableInDatabase = this.accountService.validateEmailAvailable("availableEmail@hh.se");

        // Then
        assertTrue(isEmailNotAvailableInDatabase);
    }

    @Test
    @DisplayName("validateEmailAvailable - Existing Email - Exception")
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
    @DisplayName("getAllAccountDtoViews - Success")
    void test_GetAllAccountDtos_Success() {
        // Given
        given(this.accountRepository.getAllAccountDtos()).willReturn(this.accountDtos);

        // When
        List<AccountDtoView> allAccountDtos = this.accountService.getAllAccountDtos();

        // Then
        assertThat(allAccountDtos.size()).isEqualTo(this.accountDtos.size());
        for (int i = 0; i < allAccountDtos.size(); i++) {
            if (!allAccountDtos.get(i).equals(this.accountDtos.get(i))) {
                assertEquals(allAccountDtos.get(i).email(), this.accountDtos.get(i).email());
                assertEquals(allAccountDtos.get(i).roles(), this.accountDtos.get(i).roles());
                assertEquals(allAccountDtos.get(i).number_of_jobs(), this.accountDtos.get(i).number_of_jobs());
            }
        }
    }

    @Test
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
    void test_GetAccountDtoByEmail_NonExistentEmail() {
        // Given
        given(this.accountRepository.getAccountDtoByEmail("abc")).willThrow(new ObjectNotFoundException("account", "abc"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.accountService.getAccountDtoByEmail("abc");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");

        // Verify
        then(this.accountRepository).should().getAccountDtoByEmail("abc");
    }

    @Test
    void test_SaveAccount_Success() {
        // Setup
        Account newAccount = this.accounts.get(0);

        // Given
        given(this.accountRepository.save(newAccount)).willReturn(newAccount);
        given(this.passwordEncoder.encode(newAccount.getPassword())).willReturn("Encoded Password");

        // When
        Account returnedAccount = this.accountService.save(newAccount);

        // Then
        if (!returnedAccount.equals(newAccount)) {
            assertEquals(returnedAccount.getEmail(), this.accounts.get(0).getEmail());
            assertNotNull(returnedAccount.getPassword());
            assertEquals(returnedAccount.getRoles(), this.accounts.get(0).getRoles());
            assertEquals(returnedAccount.getNumber_of_jobs(), this.accounts.get(0).getNumber_of_jobs());
        }

        // Verify
        then(this.accountRepository).should().save(newAccount);
    }

    @Test
    @DisplayName("saveAccount - Null Object (Exception) ")
    void test_SaveAccount_NullObject() {
        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.accountService.save(null));

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Account object cannot be null");
    }

    @Test
    @DisplayName("registerAccount - Success")
    void test_RegisterAccount_Success() {
        // Setup
        AccountDtoFormRegister accountDtoFormRegister = new AccountDtoFormRegister("newUser@hh.se", "123456");

        ArgumentCaptor<Account> accountArgumentCaptor = forClass(Account.class);

        // Given
        given(this.accountRepository.save(
                accountArgumentCaptor.capture()))
                .willAnswer(invocation -> invocation.getArgument(0)
                );
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
    @DisplayName("registerAccount - Null Object (Exception) ")
    void test_RegisterAccount_NullObject() {

        Throwable thrown = assertThrows(NullPointerException.class,
                () -> this.accountService.save(null));

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Account object cannot be null");
    }

    @Test
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
    void test_UpdateAccount_NonExistentId() {
        String email = "abc";
        AccountUpdateDtoForm update = new AccountUpdateDtoForm("newRoles");

        // Given
        given(this.accountRepository.findAccountByEmail(email)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            Account updatedAccount = this.accountService.update("abc", update);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");

        // Verify
        then(this.accountRepository).should().findAccountByEmail("abc");
    }

    @Test
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
    void test_DeleteAccount_NonExistentId() {
        // Given
        given(this.accountRepository.findAccountByEmail("abc")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.accountService.delete("abc");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email abc");

        // Verify
        then(this.accountRepository).should().findAccountByEmail("abc");
    }
}