package se.sprinta.headhunterbackend.accountVerification;

import lombok.ToString;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("mock-test")
class VerificationServiceTest {

    @Mock
    VerificationRepository verificationRepository;
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
    @DisplayName("POST - Send Verification Email - Success")
    void test_CreateVerification_Success() {
        // Setup
        Verification verification = this.verifications.get(0);

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

//    @Test
//    @DisplayName("POST - Send Verification Email - Success")
//    void test_SendVerificationEmail_Success() throws IOException, URISyntaxException {
//        Verification verification = this.verifications.get(0);
//        // Given
//        given(this.verificationRepository.save(this.verifications.get(0))).willReturn(verification);
//        given(this.verificationService.save(this.verifications.get(0)).
//
//                // When
//        this.verificationService.sendVerificationEmail(this.accounts.get(0));
//
//        // Verify
//        then(this.verificationRepository).should().save(this.verifications.get(0));
//    }
}