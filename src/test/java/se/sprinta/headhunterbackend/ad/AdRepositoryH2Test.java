package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.H2DatabaseInitializer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test-h2")
class AdRepositoryH2Test {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private H2DatabaseInitializer h2DbInit;

    @BeforeEach
    void setUp() {
        this.h2DbInit.initializeH2Database();
    }

    @AfterEach
    void tearDown() {
        this.h2DbInit.clearH2Database();
    }

    @Test
    @DisplayName("findAll - Success")
    void test_FindAll_Success() {
        List<Ad> allAds = this.adRepository.findAll();

        for (Ad ad : allAds) {
            System.out.println(ad.getId());
            System.out.println(ad.getHtmlCode());
            System.out.println(ad.getJob());
            System.out.println(ad.getCreateDate());
        }

        assertEquals(allAds.size(), 3);
        assertEquals(allAds.get(0).getHtmlCode(), "htmlCode 1");
        assertEquals(allAds.get(1).getHtmlCode(), "htmlCode 2");
        assertEquals(allAds.get(2).getHtmlCode(), "htmlCode 3");

        System.out.println("End of test_FindAll_Success");
    }

    @Test
    @DisplayName("getNumberOfAds - Success")
    void test_getNumberOfAds_Success() {
        List<Ad> all = this.adRepository.findAll();
        for (Ad ad : all) {
            System.out.println(ad.toString());
        }

        long numberOfAds = this.adRepository.getNumberOfAds(1L);

        assertEquals(numberOfAds, 2);
    }
}