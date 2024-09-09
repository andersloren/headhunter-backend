package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AdServiceMockTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private AdService adService;

    private List<AccountDtoView> accountDtos = new ArrayList<>();
    private List<Ad> ads = new ArrayList<>();
    private List<AdDtoView> adDtos = new ArrayList<>();
    private List<Job> jobs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.ads = MockDatabaseInitializer.initializeMockAds();
        this.adDtos = MockDatabaseInitializer.initializeMockAdDtos();
        this.jobs = MockDatabaseInitializer.initializeMockJobs();
        this.accountDtos = MockDatabaseInitializer.initializeMockAccountDtos();
    }

    @Test
    @DisplayName("Test Data Initializer")
    void test_DataInitializer() {
        System.out.println("AdServiceMockTest, ads size: " + this.ads.size());
        for (Ad ad : this.ads) {
            System.out.println(ad.toString());
        }
        System.out.println("AdServiceMockTest, adDtos size: " + this.adDtos.size());
        for (AdDtoView adDto : this.adDtos) {
            System.out.println(adDto.toString());
        }
        System.out.println("AdServiceMockTest, job size: " + this.jobs.size());
        for (Job job : this.jobs) {
            System.out.println(job.toString());
        }
    }

    @Test
    @DisplayName("GET - findAll - Success")
    void test_FindAll() {
        // Given
        given(this.adRepository.findAll()).willReturn(this.ads);

        // When
        List<Ad> foundAds = this.adService.findAll();

        // Then
        for (int i = 0; i < foundAds.size(); i++) {
            assertEquals(foundAds.size(), this.ads.size());
            assertEquals(foundAds.get(i).getId(), this.ads.get(i).getId());
            assertEquals(foundAds.get(i).getDateCreated(), this.ads.get(i).getDateCreated());
            assertEquals(foundAds.get(i).getHtmlCode(), this.ads.get(i).getHtmlCode());
        }

        // Verify
        then(this.adRepository).should().findAll();
    }

    @Test
    @DisplayName("GET - getAdDtos - Success")
    void test_GetAdDtos_Success() {
        // Given
        given(this.adRepository.getAdDtos()).willReturn(this.adDtos);

        // When
        List<AdDtoView> foundAdDtos = this.adService.getAdDtos();

        // Then
        for (int i = 0; i < foundAdDtos.size(); i++) {
            assertEquals(foundAdDtos.size(), this.adDtos.size());
            assertEquals(foundAdDtos.get(i).id(), this.adDtos.get(i).id());
            assertEquals(foundAdDtos.get(i).dateCreated(), this.adDtos.get(i).dateCreated());
            assertEquals(foundAdDtos.get(i).htmlCode(), this.adDtos.get(i).htmlCode());

            // Then
            then(this.adRepository).should().getAdDtos();
        }
    }

    @Test
    @DisplayName("GET - findById - Success")
    void test_FindById_Success() {
        // Given
        given(this.adRepository.findById("id 1")).willReturn(Optional.of(this.ads.get(0)));

        // When
        Ad foundAd = this.adService.findById("id 1");

        // Then
        assertEquals(foundAd.getId(), this.ads.get(0).getId());
        assertEquals(foundAd.getDateCreated(), this.ads.get(0).getDateCreated());
        assertEquals(foundAd.getHtmlCode(), this.ads.get(0).getHtmlCode());
        assertEquals(foundAd.getJob().getId(), this.ads.get(0).getJob().getId());

        // Verify
        then(this.adRepository).should().findById("id 1");
    }

    @Test
    @DisplayName("GET - findById - Invalid Job Id - Exception")
    void test_GetAdsByJobId_InvalidJobId_Exception() {
        // Given
        given(this.jobRepository.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdDtosByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - Success")
    void test_GetAdsByJobId_Success() {
        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(this.jobs.get(0)));
        given(this.adRepository.getAdDtosByJobId(1L)).willReturn(this.adDtos);

        // When
        List<AdDtoView> foundAds = this.adService.getAdDtosByJobId(1L);

        // Then
        assertEquals(foundAds.size(), this.adDtos.size());

        for (int i = 0; i < foundAds.size(); i++) {
            assertEquals(foundAds.get(i).id(), this.adDtos.get(i).id());
            assertEquals(foundAds.get(i).dateCreated(), this.adDtos.get(i).dateCreated());
            assertEquals(foundAds.get(i).htmlCode(), this.adDtos.get(i).htmlCode());
        }

        // Verify
        then(this.adRepository).should().getAdDtosByJobId(1L);
    }

    @Test
    @DisplayName("GET - getAdDtosByJobId - Invalid Job Id - Exception")
    void test_GetAdDtosByJobId_InvalidJobId_Exception() {
        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdDtosByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("GET - getAccountDtoByAdId - Success")
    void test_GetAccountDtoByAdId_Success() {
        // Given
        given(this.adRepository.getAccountDtoByAdId("id 1")).willReturn(this.accountDtos.get(0));

        // When
        AccountDtoView foundAccountDto = this.adRepository.getAccountDtoByAdId("id 1");

        // Then
        assertEquals(foundAccountDto.email(), this.accountDtos.get(0).email());

        // Verify
        then(this.adRepository).should().getAccountDtoByAdId("id 1");
    }

    @Test
    @DisplayName("GET - getAccountDtoByAdId - Invalid Ad Id - Exception")
    void test_GetAccountDtoByAdId_InvalidAdId_Exception() {
        Account account = new Account();
        account.setEmail("user1@hh.se");

        // Given
        given(this.adRepository.findById("invalid id")).willThrow(new ObjectNotFoundException("ad", "invalid id"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAccountDtoByAdId("invalid id"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id invalid id");

        // Verify
        then(this.adRepository).should().findById("invalid id");
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - Success")
    void test_getNumberOfAdsByJobId_Success() {
        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(this.jobs.get(0)));
        given(this.adRepository.getNumberOfAdsByJobId(1L)).willReturn(2L);

        // When
        long numberOfAds = this.adService.getNumberOfAdsByJobId(1L);

        // Then
        assertEquals(numberOfAds, 2);

        // Verify
        then(this.jobRepository).should().findById(1L);
    }

    @Test
    @DisplayName("GET - getNumberOfAdsByJobId - Invalid Job Id - Exception")
    void test_GetNumberOfAdsByJobId_InvalidJobId_Exception() {
        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getNumberOfAdsByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("POST - addAd - Success")
    void test_AddAd_Success() {
        AdDtoForm adDtoForm = new AdDtoForm("htmlCode 1");

        ArgumentCaptor<Ad> adArgumentCaptor = forClass(Ad.class);

        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(this.jobs.get(0)));
        given(this.adRepository.save(adArgumentCaptor.capture())).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Ad savedAd = this.adService.addAd(1L, adDtoForm);
        System.out.println(savedAd.getHtmlCode());
        System.out.println(savedAd.getDateCreated());

        // Then
        Ad capturedAd = adArgumentCaptor.getValue();

        assertEquals(savedAd.getHtmlCode(), capturedAd.getHtmlCode());
        assertEquals(savedAd.getDateCreated(), capturedAd.getDateCreated());

        // Then
        then(this.jobRepository).should().findById(1L);
        then(this.adRepository).should().save(adArgumentCaptor.capture());
    }

    @Test
    @DisplayName("POST - addAd - Invalid Job Id - Exception")
    void test_AddAd_InvalidJobId_Exception() {
        AdDtoForm adDtoForm = new AdDtoForm("htmlCode 1");

        // Given
        given(this.jobRepository.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("ad", Long.MAX_VALUE));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.addAd(Long.MAX_VALUE, adDtoForm));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id " + Long.MAX_VALUE);

        // Then
        then(this.jobRepository).should().findById(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("DELETE - delete - Invalid Ad Id - Exception")
    void test_Delete_Success() {
        // Given
        given(this.adRepository.findById("invalid id")).willThrow(new ObjectNotFoundException("ad", "invalid id"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.delete("invalid id"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id invalid id");

        // Verify
        then(this.adRepository).should().findById("invalid id");
    }
}
