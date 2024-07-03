package se.sprinta.headhunterbackend.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountServiceComplmentaryH2Test {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private H2DatabaseInitializer dbInit;

    @BeforeEach
    void setUp() {
        this.dbInit.initializeH2Database();
    }

    @Test
    @DisplayName("delete - Success")
    void test_Delete_Success() {
        this.accountService.delete("user1-h2@hh.se");

        assertThrows(ObjectNotFoundException.class, () -> this.accountService.findById("user1-h2@hh.se"));
    }
}
