package se.sprinta.headhunterbackend.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.verification.Verification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles("mock-test")
@ExtendWith(MockitoExtension.class)
class MessagesMockTest {

    List<Account> accounts = new ArrayList<>();
    List<Verification> verifications = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
        this.verifications = MockDatabaseInitializer.initializeMockVerifications();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_DataInitializer() {
        System.out.println("MessageMockTest, accounts size: " + this.accounts.size());
        for (Account account : this.accounts) {
            System.out.println(account.toString());
        }
    }

    @Test
    @DisplayName("VerificationEmail - Success")
    void test_VerificationEmail_Success() {

        Map<String, Object> emailPayLoad = new HashMap<>();
        Map<String, Object> message = new HashMap<>();


    }

}
