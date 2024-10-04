package se.sprinta.headhunterbackend.verification;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.TestsDatabaseInitializer;
import se.sprinta.headhunterbackend.account.Account;
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
public class VerificationServiceTest {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private TestsDatabaseInitializer h2DbInit;

    List<Verification> verifications = new ArrayList<>();
    List<Account> accounts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.h2DbInit.initializeH2Database();
        this.verifications = TestsDatabaseInitializer.getVerifications();
        this.accounts = TestsDatabaseInitializer.getAccounts();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearH2Database();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_databaseInitializer() {
        assertEquals(3, this.verifications.size());
        assertEquals(4, this.accounts.size());
    }

    @Test
    @DisplayName("GET - Find All - Success")
    void test_FindAll_Success() {
        // When
        List<Verification> foundVerifications = this.verificationService.findAll();

        // Then
        assertEquals(this.verifications.size(), foundVerifications.size());

        for (int i = 0; i < foundVerifications.size(); i++) {
            assertEquals(this.verifications.get(i).getId(),
                    foundVerifications.get(i).getId());
            assertEquals(this.verifications.get(i).getVerificationCode(),
                    foundVerifications.get(i).getVerificationCode());
            assertEquals(this.verifications.get(i).getAccount().getEmail(),
                    foundVerifications.get(i).getAccount().getEmail());
            assertEquals(this.verifications.get(i).getAccount().getRoles(),
                    foundVerifications.get(i).getAccount().getRoles());
            assertEquals(this.verifications.get(i).getAccount().getNumber_of_jobs(),
                    foundVerifications.get(i).getAccount().getNumber_of_jobs());
            assertEquals(this.verifications.get(i).getAccount().isVerified(),
                    foundVerifications.get(i).getAccount().isVerified());
        }
    }

    @Test
    @DisplayName("POST - Verify Registration - Success")
    void test_VerifyRegistration_Success() throws IOException, URISyntaxException {
        // When
        this.verificationService.verifyRegistration(this.accounts.get(0).getEmail(),
                this.verifications.get(0).getVerificationCode());

        // Then
        List<Verification> foundVerifications = this.verificationService.findAll();

        assertEquals(foundVerifications.size(), this.verifications.size() - 1);
    }

    @Test
    @DisplayName("POST - Verify Registration - Non Existent Email - Exception")
    void test_VerifyRegistration_NonExistentEmail_Exception() throws IOException, URISyntaxException {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.verificationService.verifyRegistration("Invalid Email",
                        this.verifications.get(0).getVerificationCode()));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find account with Email Invalid Email");
    }

    @Test
    @DisplayName("POST - Verify Registration - Non Existent Verification Code - Exception")
    void test_VerifyRegistration_NonExistentVerificationCode_Exception() throws IOException, URISyntaxException {
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.verificationService.verifyRegistration(this.accounts.get(3).getEmail(), "12345"));

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find verification code with Id " + this.accounts.get(3).getEmail());
    }
}
