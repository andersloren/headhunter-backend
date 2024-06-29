package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.*;
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

    @Test
    void test_FindAll_Success() {
        // Given
        given(this.accountRepository.findAll()).willReturn(this.accounts);

        // When
        List<Account> actualAccounts = this.accountService.findAll();
        System.out.println(actualAccounts.size());

        // Then
        assertThat(actualAccounts.size()).isEqualTo(this.accounts.size());

        // Verify
        then(this.accountRepository).should().findAll();
    }

    @Test
    @DisplayName("checkEmailUnique - Non-Existing Email")
    void test_CheckEmailUnique_NonExistingEmail_ReturnsTrue_Success() {
        // Given
        given(this.accountRepository.checkEmailUnique("availableEmail@hh.se")).willReturn(true);

        // When
        boolean isEmailNotAvailableInDatabase = this.accountService.checkEmailUnique("availableEmail@hh.se");

        // Then
        assertTrue(isEmailNotAvailableInDatabase);
    }

    @Test
    @DisplayName("checkEmailUnique - Existing Email - Exception")
    void test_CheckEmailUnique_ExistingEmail_Exception() {
        // Given
        given(this.accountRepository.checkEmailUnique("admin@hh.se")).willReturn(false);

        Throwable thrown = assertThrows(EmailNotFreeException.class,
                () -> this.accountService.checkEmailUnique("admin@hh.se"));

        assertThat(thrown)
                .isInstanceOf(EmailNotFreeException.class)
                .hasMessage("admin@hh.se is already registered");
    }


    @Test
    void test_GetAccountByEmail_Success() {
        AccountDtoView accountDtoView = new AccountDtoView(
                "admin@hh.se",
                "admin",
                0
        );
        given(this.accountRepository.getAccountDtoByEmail("admin@hh.se")).willReturn(Optional.of(accountDtoView));

        // When
        AccountDtoView returnedAccountDtoView = this.accountService.getAccountDtoByEmail("admin@hh.se");

        // Then
        assertEquals(returnedAccountDtoView.email(), "admin@hh.se");
        assertEquals(returnedAccountDtoView.roles(), "admin");

        // Verify
        then(this.accountRepository).should().getAccountDtoByEmail("admin@hh.se");
    }

    @Test
    void test_GetAccountByEmail_NonExistentEmail() {
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
    void test_GetAccountDtoByEmail_Success() {
        AccountDtoView accountDtoView = new AccountDtoView(
                "admin@hh.se",
                "admin",
                0
        );

        given(this.accountRepository.getAccountDtoByEmail("admin@hh.se")).willReturn(Optional.of(accountDtoView));

        // When
        AccountDtoView returnedAccountDtoView = this.accountService.getAccountDtoByEmail("admin@hh.se");

        // Then
        assertEquals(returnedAccountDtoView.email(), "admin@hh.se");
        assertEquals(returnedAccountDtoView.roles(), "admin");
        assertEquals(returnedAccountDtoView.number_of_jobs(), 0);

        // Verify
        then(this.accountRepository).should().getAccountDtoByEmail("admin@hh.se");
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
        Account newAccount = new Account();
        newAccount.setEmail("admin@hh.se");
        newAccount.setPassword("123456");
        newAccount.setRoles("admin");

        // Given
        given(this.accountRepository.save(newAccount)).willReturn(newAccount);
        given(this.passwordEncoder.encode(newAccount.getPassword())).willReturn("Encoded Password");

        // When
        Account returnedAccount = this.accountService.save(newAccount);

        // Then
        assertEquals(returnedAccount.getEmail(), "admin@hh.se");
        assertEquals(returnedAccount.getRoles(), "admin");

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

        assertEquals(returnedAccount.getEmail(), capturedAccount.getEmail());
        assertEquals(returnedAccount.getRoles(), capturedAccount.getRoles());

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
        Account ownAccount = new Account();
        ownAccount.setEmail("admin.se");
        ownAccount.setPassword("123456");
        ownAccount.setRoles("admin");

        AccountUpdateDtoForm accountUpdateDtoForm = new AccountUpdateDtoForm("user"); // From admin user to just admin

        String email = "admin@hh.se";
        String roles = "user";

        // Given
        given(this.accountRepository.findAccountByEmail(email)).willReturn(Optional.of(ownAccount));
        given(this.accountRepository.save(ownAccount)).willReturn(ownAccount);

        // When
        Account updatedAccount = this.accountService.update(email, accountUpdateDtoForm);

        // Then
        assertThat(updatedAccount.getEmail()).isEqualTo(ownAccount.getEmail());
        assertThat(updatedAccount.getRoles()).isEqualTo(roles);

        // Verify
        then(this.accountRepository).should().findAccountByEmail(email);
        then(this.accountRepository).should().save(ownAccount);
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
        Account account = new Account();
        account.setEmail("user@hh.se");
        account.setRoles("user");

        // Given
        given(this.accountRepository.findAccountByEmail("user@hh.se")).willReturn(Optional.of(account));
        willDoNothing().given(this.accountRepository).delete(account);

        // When
        this.accountService.delete("user@hh.se");

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