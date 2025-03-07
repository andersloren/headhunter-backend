package se.sprinta.headhunterbackend.verification;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountRepository;
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

@SpringBootTest
@ActiveProfiles("mock-test")
class VerificationServiceMockTest {

    @Mock
    VerificationRepository verificationRepository;
    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    VerificationService verificationService;

    List<Verification> verifications = new ArrayList<>();
    List<Account> accounts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.verifications = MockDatabaseInitializer.initializeMockVerifications();
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
    }

    @Test
    @DisplayName("Test Data Initializer")
    void test_DataInitializer() {
        System.out.println("VerificationServiceMocktest, verifications size: " + this.verifications.size());
        for (Verification verification : this.verifications) {
            System.out.println(verification.toString());
        }
    }

    @Test
    @DisplayName("GET - Find All - Success")
    void test_FindAll_Success() {
        // Given
        given(this.verificationRepository.findAll()).willReturn(this.verifications);

        // When
        List<Verification> actualVerifications = this.verificationService.findAll();

        // Then
        assertThat(actualVerifications.size()).isEqualTo(this.verifications.size());
        for (int i = 0; i < actualVerifications.size(); i++) {
            if (!actualVerifications.get(i).equals(this.verifications.get(i))) {
                assertEquals(actualVerifications.get(i).getId(), this.verifications.get(i).getId());
                assertEquals(actualVerifications.get(i).getVerificationCode(), this.verifications.get(i).getVerificationCode());
                assertEquals(actualVerifications.get(i).getAccount(), this.verifications.get(i).getAccount());
            }
        }

        // Verify
        then(this.verificationRepository).should().findAll();
    }

    @Test
    @DisplayName("POST - Create Verification Email - Success")
    void test_CreateVerification_Success() {
        // Setup
        ArgumentCaptor<Verification> verificationArgumentCaptor = forClass(Verification.class);

        // Given
        given(this.verificationRepository.save(
                verificationArgumentCaptor.capture()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        String returnedVerificationCode = this.verificationService.createVerification(this.accounts.get(0));

        // Then
        Verification capturedVerification = verificationArgumentCaptor.getValue();

        assertEquals(returnedVerificationCode, capturedVerification.getVerificationCode());

        // Verify
        then(this.verificationRepository).should().save(verificationArgumentCaptor.capture());
    }

    @Test
    @DisplayName("POST - Verify Registration - Success")
    void test_VerifyRegistration_Success() throws IOException, URISyntaxException {
        // Given
        given(this.accountRepository.findAccountByEmail(this.accounts.get(0).getEmail())).willReturn(Optional.of(this.accounts.get(0)));
        given(this.verificationRepository.findByEmail(this.accounts.get(0).getEmail())).willReturn(Optional.of(this.verifications.get(0).getVerificationCode()));
        willDoNothing().given(this.verificationRepository).deleteByEmail(this.accounts.get(0).getEmail());

        // When
        this.verificationService.verifyRegistration(this.accounts.get(0).getEmail(), this.verifications.get(0).getVerificationCode());

        // Then
        then(this.verificationRepository).should().deleteByEmail(this.accounts.get(0).getEmail());
    }

    @Test
    @DisplayName("POST - Verify Registration - Non Existent Account - Exception")
    void test_VerifyRegistration_NonExistentAccount_Exception() {
        // Given
        given(this.accountRepository.findAccountByEmail(this.accounts.get(0).getEmail())).willThrow(new ObjectNotFoundException("account", this.accounts.get(0).getEmail()));

        // Then
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.accountRepository.findAccountByEmail(this.accounts.get(0).getEmail()));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email " + this.accounts.get(0).getEmail());

        // Verify
        then(this.accountRepository).should().findAccountByEmail(this.accounts.get(0).getEmail());
    }

    @Test
    @DisplayName("POST - Verify Registration - Non Existent Verification - Exception")
    void test_VerifyRegistration_NonExistentVerification_Exception() {
        // Given
        given(this.verificationRepository.findByEmail(this.accounts.get(0).getEmail())).willThrow(new ObjectNotFoundException("verification code", this.accounts.get(0).getEmail()));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.verificationRepository.findByEmail(this.accounts.get(0).getEmail()));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find verification code with Id " + this.accounts.get(0).getEmail());

        // Verify
        then(this.verificationRepository).should().findByEmail(this.accounts.get(0).getEmail());
    }

    @Test
    @DisplayName("DELETE - Delete Verification - Success")
    void test_DeleteVerification_Success() {
        // Given
        given(this.verificationRepository.findVerificationByEmail(this.accounts.get(0).getEmail())).willReturn(Optional.of(this.verifications.get(0)));
        willDoNothing().given(this.verificationRepository).delete(this.verifications.get(0));

        // When
        this.verificationService.delete(this.accounts.get(0).getEmail());

        // Then
        then(this.verificationRepository).should().delete(this.verifications.get(0));
    }

    @Test
    @DisplayName("DELETE - Delete Verification - Non Existent Verification - Exception")
    void test_DeleteVerification_NonExistentVerification_Exception() {
        // Given
        given(this.verificationRepository.findVerificationByEmail(this.accounts.get(0).getEmail())).willThrow(new ObjectNotFoundException("verification", this.accounts.get(0).getEmail()));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.verificationRepository.findVerificationByEmail(this.accounts.get(0).getEmail()));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find verification with Id " + this.accounts.get(0).getEmail());

        // Verify
        then(this.verificationRepository).should().findVerificationByEmail(this.accounts.get(0).getEmail());
    }
}

