package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.TestsDatabaseInitializer;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceComplementaryTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestsDatabaseInitializer h2DbInit;

    @BeforeEach
    void setUp() {
        this.h2DbInit.initializeH2Database();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearH2Database();
    }

    @Test
    @DisplayName("delete - Success")
    void test_Delete_Success() {
        this.accountService.delete("user1-h2@hh.se");

        assertThrows(ObjectNotFoundException.class, () -> this.accountService.findById("user1-h2@hh.se"));
    }
}
