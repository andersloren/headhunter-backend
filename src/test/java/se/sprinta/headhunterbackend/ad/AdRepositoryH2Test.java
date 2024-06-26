package se.sprinta.headhunterbackend.ad;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test-h2")
@Transactional
class AdRepositoryH2Test {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private H2DatabaseInitializer dbInit;

    @BeforeEach
    void setUp() {
        this.dbInit.initializeDatabase();
    }

    @AfterEach
    void tearDown() {
        this.dbInit.clearDatabase();
    }

    @Test
    @DisplayName("getNumberOfAds - Success")
    @Order(2)
    void test_getNumberOfAds_Success() {
        long numberOfAds = this.adRepository.getNumberOfAds(1L);

        Assertions.assertEquals(numberOfAds, 2);
    }
}