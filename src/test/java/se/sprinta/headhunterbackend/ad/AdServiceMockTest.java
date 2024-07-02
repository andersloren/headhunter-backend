package se.sprinta.headhunterbackend.ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class AdServiceMockTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private AdService adService;

    List<Ad> ads = new ArrayList<>();
    List<AdDtoView> adDtos = new ArrayList<>();
    List<Job> jobs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Ad ad1 = new Ad();
        ad1.setId("id 1");
        ad1.setHtmlCode("htmlCode 1");

        Ad ad2 = new Ad();
        ad2.setId("id 2");
        ad2.setHtmlCode("htmlCode 2");

        Ad ad3 = new Ad();
        ad3.setId("id 3");
        ad3.setHtmlCode("htmlCode 3");

        Job job1 = new Job();
        job1.setId(1L);

        Job job2 = new Job();
        job2.setId(2L);

        this.jobs.add(job1);
        this.jobs.add(job2);

        ad1.setJob(job1);
        ad2.setJob(job1);
        ad3.setJob(job2);

        this.ads.add(ad1);
        this.ads.add(ad2);
        this.ads.add(ad3);

        AdDtoView adDtoView1 = new AdDtoView(
                "id 1",
                ad1.getCreatedDateTime(),
                "htmlCode 1"
        );

        AdDtoView adDtoView2 = new AdDtoView(
                "id 2",
                ad2.getCreatedDateTime(),
                "htmlCode 2"
        );

        AdDtoView adDtoView3 = new AdDtoView(
                "id 3",
                ad3.getCreatedDateTime(),
                "htmlCode 3"
        );

        this.adDtos.add(adDtoView1);
        this.adDtos.add(adDtoView2);
        this.adDtos.add(adDtoView3);
    }

    @Test
    @DisplayName("findAll - Success()")
    void test_FindAll() {
        // Given
        given(this.adRepository.findAll()).willReturn(this.ads);

        // When
        List<Ad> foundAds = this.adService.findAll();
        for (Ad ad : foundAds) {
            System.out.println(ad.getId());
            System.out.println(ad.getCreatedDateTime());
            System.out.println(ad.getHtmlCode());
            System.out.println(ad.getJob().getId());
        }

        // Then
        assertEquals(foundAds.size(), this.ads.size());
        assertEquals(foundAds.get(0).getId(), this.ads.get(0).getId());
        assertEquals(foundAds.get(0).getCreatedDateTime(), this.ads.get(0).getCreatedDateTime());
        assertEquals(foundAds.get(0).getHtmlCode(), this.ads.get(0).getHtmlCode());
        assertEquals(foundAds.get(1).getId(), this.ads.get(1).getId());
        assertEquals(foundAds.get(1).getCreatedDateTime(), this.ads.get(1).getCreatedDateTime());
        assertEquals(foundAds.get(1).getHtmlCode(), this.ads.get(1).getHtmlCode());
        assertEquals(foundAds.get(2).getId(), this.ads.get(2).getId());
        assertEquals(foundAds.get(2).getCreatedDateTime(), this.ads.get(2).getCreatedDateTime());
        assertEquals(foundAds.get(2).getHtmlCode(), this.ads.get(2).getHtmlCode());

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
            System.out.println(adDto.createdDateTime());
            System.out.println(adDto.htmlCode());
        }

        // Then
        assertEquals(foundAdDtos.size(), this.adDtos.size());
        assertEquals(foundAdDtos.get(0).id(), this.adDtos.get(0).id());
        assertEquals(foundAdDtos.get(0).createdDateTime(), this.adDtos.get(0).createdDateTime());
        assertEquals(foundAdDtos.get(0).htmlCode(), this.adDtos.get(0).htmlCode());
        assertEquals(foundAdDtos.get(1).id(), this.adDtos.get(1).id());
        assertEquals(foundAdDtos.get(1).createdDateTime(), this.adDtos.get(1).createdDateTime());
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
        assertEquals(foundAd.getCreatedDateTime(), this.ads.get(0).getCreatedDateTime());
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
        this.ads.remove(2);

        // Given
        given(this.adRepository.getAdsByJobId(1L)).willReturn(this.ads);

        // When
        List<Ad> foundAds = this.adService.getAdsByJobId(1L);
        for (Ad ad : foundAds) {
            System.out.println(ad.getId());
            System.out.println(ad.getCreatedDateTime());
            System.out.println(ad.getHtmlCode());
            System.out.println(ad.getJob().getId());
        }

        // Then
        assertEquals(foundAds.size(), this.ads.size());
        assertEquals(foundAds.get(0).getId(), this.ads.get(0).getId());
        assertEquals(foundAds.get(0).getCreatedDateTime(), this.ads.get(0).getCreatedDateTime());
        assertEquals(foundAds.get(0).getHtmlCode(), this.ads.get(0).getHtmlCode());
        assertEquals(foundAds.get(1).getId(), this.ads.get(1).getId());
        assertEquals(foundAds.get(1).getCreatedDateTime(), this.ads.get(1).getCreatedDateTime());
        assertEquals(foundAds.get(1).getHtmlCode(), this.ads.get(1).getHtmlCode());
        assertEquals(foundAds.get(1).getId(), this.ads.get(1).getId());
        assertEquals(foundAds.get(1).getCreatedDateTime(), this.ads.get(1).getCreatedDateTime());
        assertEquals(foundAds.get(1).getHtmlCode(), this.ads.get(1).getHtmlCode());

        // Verify
        then(this.adRepository).should().getAdsByJobId(1L);
    }

    @Test
    @DisplayName("findById - Invalid Job Id - Exception")
    void test_GetAdsByJobId_InvalidJobId_Exception() {
        // Given
        given(this.adRepository.getAdsByJobId(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdsByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

        // Verify
        then(this.adRepository).should().getAdsByJobId(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("getAdDtosByJobId - Success")
    void test_GetAdDtossByJobId_Success() {
        // Given
        given(this.adRepository.getAdDtosByJobId(1L)).willReturn(this.adDtos);

        // When
        List<AdDtoView> foundAdDtos = this.adService.getAdDtosByJobId(1L);
        for (AdDtoView adDto : foundAdDtos) {
            System.out.println(adDto.id());
            System.out.println(adDto.createdDateTime());
            System.out.println(adDto.htmlCode());
        }

        // Then
        assertEquals(foundAdDtos.size(), this.adDtos.size());
        assertEquals(foundAdDtos.get(0).id(), this.adDtos.get(0).id());
        assertEquals(foundAdDtos.get(0).createdDateTime(), this.adDtos.get(0).createdDateTime());
        assertEquals(foundAdDtos.get(0).htmlCode(), this.adDtos.get(0).htmlCode());
        assertEquals(foundAdDtos.get(1).id(), this.adDtos.get(1).id());
        assertEquals(foundAdDtos.get(1).createdDateTime(), this.adDtos.get(1).createdDateTime());
        assertEquals(foundAdDtos.get(1).htmlCode(), this.adDtos.get(1).htmlCode());

        // Verify
        then(this.adRepository).should().getAdDtosByJobId(1L);
    }

    @Test
    @DisplayName("getAdDtosByJobId - Invalid Job Id - Exception")
    void test_GetAdDtosByJobId_InvalidJobId_Exception() {
        // Given
        given(this.adRepository.getAdDtosByJobId(Long.MAX_VALUE)).willThrow(new ObjectNotFoundException("job", Long.MAX_VALUE));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAdDtosByJobId(Long.MAX_VALUE));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find job with Id " + Long.MAX_VALUE);

        // Verify
        then(this.adRepository).should().getAdDtosByJobId(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("getAccountByAdId - Success")
    void test_GetAccountByAdId() {
        Ad ad = new Ad();
        ad.setId("id 1");
        ad.setHtmlCode("html 1");

        Job job = new Job();
        job.setId(1L);

        ad.setJob(job);

        Account account = new Account();
        account.setEmail("user1@hh.se");

        job.setAccount(account);

        // Given
        given(this.adRepository.getAccountByAdId("id 1")).willReturn(account);

        // When
        Account foundAccount = this.adRepository.getAccountByAdId("id 1");

        // Then
        assertEquals(foundAccount.getEmail(), account.getEmail());

        // Verify
        then(this.adRepository).should().getAccountByAdId("id 1");
    }

    @Test
    @DisplayName("getAccountByAdId - Invalid Ad Id - Exception")
    void test_GetAccountByAdId_InvalidAdId_Exception() {
        Account account = new Account();
        account.setEmail("user1@hh.se");

        // Given
        given(this.adRepository.findById("invalid id")).willThrow(new ObjectNotFoundException("ad", "invalid id"));

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class,
                () -> this.adService.getAccountByAdId("invalid id"));

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
        System.out.println(savedAd.getCreatedDateTime());

        // Then
        Ad capturedAd = adArgumentCaptor.getValue();

        assertEquals(savedAd.getHtmlCode(), capturedAd.getHtmlCode());
        assertEquals(savedAd.getCreatedDateTime(), capturedAd.getCreatedDateTime());

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
