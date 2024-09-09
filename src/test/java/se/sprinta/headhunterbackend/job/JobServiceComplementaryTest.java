package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.TestsDatabaseInitializer;

import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class JobServiceComplementaryTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestsDatabaseInitializer h2DbInit;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE job ALTER COLUMN id RESTART WITH 1");

        this.h2DbInit.initializeH2Database();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearH2Database();
    }

    @Test
    @DisplayName("delete - Success")
    void test_Delete_Success() {

        this.jobService.delete("user1-test@hh.se", 1L);

        assertThrows(ObjectNotFoundException.class,
                () -> this.jobService.findById(1L));
    }
}
