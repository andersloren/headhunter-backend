package se.sprinta.headhunterbackend.job;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test-h2")
public class JobServiceComplementaryH2Test {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private H2DatabaseInitializer h2DbInit;

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
        List<Job> allJobsBeforeDelete = this.jobRepository.findAll();
        allJobsBeforeDelete.forEach(job ->
                System.out.println(job.getId())
        );

        this.jobService.delete("user1-h2@hh.se", 1L);

        List<Job> allJobsAfterDelete = this.jobRepository.findAll();
        allJobsAfterDelete.forEach(job ->
                System.out.println(job.getId())
        );
    }
}
