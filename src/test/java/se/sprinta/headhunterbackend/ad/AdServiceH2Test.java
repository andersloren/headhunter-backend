package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test-h2")
public class AdServiceH2Test {

    @Mock
    private AdRepository adRepository;

    @InjectMocks
    private AdService adService;

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
    @DisplayName("findAll - Success")
    void test_FindAll_Success() {
        List<Ad> allAds = this.adService.findAll();

        assertEquals(allAds.size(), 3);

        assertEquals(allAds.get(0).getHtmlCode(), "htmlCode 1");
        assertTrue(allAds.get(0).getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertEquals(allAds.get(1).getHtmlCode(), "htmlCode 2");
        assertTrue(allAds.get(1).getCreatedDateTime().isBefore(ZonedDateTime.now()));
        assertEquals(allAds.get(2).getHtmlCode(), "htmlCode 3");
        assertTrue(allAds.get(2).getCreatedDateTime().isBefore(ZonedDateTime.now()));
    }

}
