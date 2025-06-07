package se.sprinta.headhunterbackend.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.Account;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("mock-test")
@ExtendWith(MockitoExtension.class)
class MessagesMockTest {

    List<Account> accounts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_DataInitializer() {
        System.out.println("MessageMockTest, accounts size: " + this.accounts.size());
        for (Account account : this.accounts) {
            System.out.println(account.toString());
        }
    }

}
