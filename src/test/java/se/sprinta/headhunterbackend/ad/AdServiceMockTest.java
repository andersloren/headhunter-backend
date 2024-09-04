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

import java.time.LocalDate;
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
    @DisplayName("findAll - Success()")
    void test_FindAll() {
        for (Ad ad : this.ads) {
            System.out.println(ad.getId());
        }

        // Given
        given(this.adRepository.findAll()).willReturn(this.ads);

        // When
        List<Ad> foundAds = this.adService.findAll();
        for (Ad ad : foundAds) {
            System.out.println(ad.getId());
            System.out.println(ad.getDateCreated());
            System.out.println(ad.getHtmlCode());
            System.out.println(ad.getJob().getId());
        }

        // Then
        assertEquals(foundAds.size(), this.ads.size());
        assertEquals(foundAds.get(0).getId(), this.ads.get(0).getId());
        assertEquals(foundAds.get(0).getDateCreated(), this.ads.get(0).getDateCreated());
        assertEquals(foundAds.get(0).getHtmlCode(), this.ads.get(0).getHtmlCode());
        assertEquals(foundAds.get(1).getId(), this.ads.get(1).getId());
        assertEquals(foundAds.get(1).getDateCreated(), this.ads.get(1).getDateCreated());
        assertEquals(foundAds.get(1).getHtmlCode(), this.ads.get(1).getHtmlCode());
        assertEquals(foundAds.get(2).getId(), this.ads.get(2).getId());
        assertEquals(foundAds.get(2).getDateCreated(), this.ads.get(2).getDateCreated());
        assertEquals(foundAds.get(2).getHtmlCode(), this.ads.get(2).getHtmlCode());

        System.out.print("assertEquals: " + foundAds.get(0).getId() + " - ");
        System.out.println(this.ads.get(0).getId());

        // Verify
        then(this.adRepository).should().findAll();
    }

    @Test
    @DisplayName("getAllAdDtos - Success")
    void test_GetAllAdDtos_Success() {
        // Given
        given(this.adRepository.getAllAdDtos()).willReturn(this.adDtos);

        // When
        List<AdDtoView> foundAdDtos = this.adService.getAllAdDtos();
        for (AdDtoView adDto : foundAdDtos) {
            System.out.println(adDto.id());
            System.out.println(adDto.dateCreated());
            System.out.println(adDto.htmlCode());
        }

        // Then
        assertEquals(foundAdDtos.size(), this.adDtos.size());
        assertEquals(foundAdDtos.get(0).id(), this.adDtos.get(0).id());
        assertEquals(foundAdDtos.get(0).dateCreated(), this.adDtos.get(0).dateCreated());
        assertEquals(foundAdDtos.get(0).htmlCode(), this.adDtos.get(0).htmlCode());
        assertEquals(foundAdDtos.get(1).id(), this.adDtos.get(1).id());
        assertEquals(foundAdDtos.get(1).dateCreated(), this.adDtos.get(1).dateCreated());
        assertEquals(foundAdDtos.get(1).htmlCode(), this.adDtos.get(1).htmlCode());

        // Then
        then(this.adRepository).should().getAllAdDtos();
    }

    @Test
    @DisplayName("findById - Sucess")
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
    @DisplayName("findById - Invalid Ad Id - Exception")
    void test_FindById_InvalidAdId_Exception() {
        // Given
        given(this.adRepository.findById("invalid id")).willThrow(new ObjectNotFoundException("ad", "invalid id"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.findById("invalid id"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find ad with Id invalid id");

        // Verify
        then(this.adRepository).should().findById("invalid id");
    }

    @Test
    @DisplayName("getAdsByJobId - Success")
    void test_GetAdsByJobId_Success() {
        System.out.println("getAdsByJobId - ads before remove()");
        for (Ad ad : this.ads) {
            System.out.println(ad.toString());
        }
        System.out.println("End");

        this.ads.remove(2);

        System.out.println("getAdsByJobId - ads after remove()");
        for (Ad ad : this.ads) {
            System.out.println(ad.toString());
        }
        System.out.println("End");

        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(this.jobs.get(0)));
        given(this.adRepository.getAdsByJobId(1L)).willReturn(this.ads);

        // When
        List<Ad> foundAds = this.adService.getAdsByJobId(1L);
        for (Ad ad : foundAds) {
            System.out.println(ad.getId());
            System.out.println(ad.getDateCreated());
            System.out.println(ad.getHtmlCode());
            System.out.println(ad.getJob().getId());
        }

        // Then
        assertEquals(foundAds.size(), this.ads.size());
        assertEquals(foundAds.get(0).getId(), this.ads.get(0).getId());
        assertEquals(foundAds.get(0).getDateCreated(), this.ads.get(0).getDateCreated());
        assertEquals(foundAds.get(0).getHtmlCode(), this.ads.get(0).getHtmlCode());
        assertEquals(foundAds.get(1).getId(), this.ads.get(1).getId());
        assertEquals(foundAds.get(1).getDateCreated(), this.ads.get(1).getDateCreated());
        assertEquals(foundAds.get(1).getHtmlCode(), this.ads.get(1).getHtmlCode());
        assertEquals(foundAds.get(1).getId(), this.ads.get(1).getId());
        assertEquals(foundAds.get(1).getDateCreated(), this.ads.get(1).getDateCreated());
        assertEquals(foundAds.get(1).getHtmlCode(), this.ads.get(1).getHtmlCode());

        // Verify
        then(this.adRepository).should().getAdsByJobId(1L);
    }

    @Test
    @DisplayName("findById - Invalid Job Id - Exception")
    void test_GetAdsByJobId_InvalidJobId_Exception() {
        // Given
        given(this.jobRepository.findById(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdsByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("getAdDtosByJobId - Success")
    void test_GetAdDtossByJobId_Success() {
        // Given
        given(this.jobRepository.findById(1L)).willReturn(Optional.of(this.jobs.get(0)));
        given(this.adRepository.getAdDtosByJobId(1L)).willReturn(this.adDtos);

        // When
        List<AdDtoView> foundAdDtos = this.adService.getAdDtosByJobId(1L);
        for (AdDtoView adDto : foundAdDtos) {
            System.out.println(adDto.id());
            System.out.println(adDto.dateCreated());
            System.out.println(adDto.htmlCode());
        }

        // Then
        assertEquals(foundAdDtos.size(), this.adDtos.size());
        assertEquals(foundAdDtos.get(0).id(), this.adDtos.get(0).id());
        assertEquals(foundAdDtos.get(0).dateCreated(), this.adDtos.get(0).dateCreated());
        assertEquals(foundAdDtos.get(0).htmlCode(), this.adDtos.get(0).htmlCode());
        assertEquals(foundAdDtos.get(1).id(), this.adDtos.get(1).id());
        assertEquals(foundAdDtos.get(1).dateCreated(), this.adDtos.get(1).dateCreated());
        assertEquals(foundAdDtos.get(1).htmlCode(), this.adDtos.get(1).htmlCode());

        // Verify
        then(this.adRepository).should().getAdDtosByJobId(1L);
    }

    @Test
    @DisplayName("getAdDtosByJobId - Invalid Job Id - Exception")
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
    @DisplayName("getAccountDtoByAdId - Success")
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
    @DisplayName("getAccountDtoByAdId - Invalid Ad Id - Exception")
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
    @DisplayName("addAd - Success")
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
    @DisplayName("addAd - Invalid Job Id - Exception")
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
    @DisplayName("delete - Invalid Ad Id - Exception")
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
