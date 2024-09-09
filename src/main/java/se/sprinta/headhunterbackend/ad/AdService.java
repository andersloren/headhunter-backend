package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.List;

/**
 * Business logic for Ad
 */

@Service
@Transactional
public class AdService {

  private final AdRepository adRepository;
  private final JobRepository jobRepository;

  public AdService(AdRepository adRepository, JobRepository jobRepository) {
    this.adRepository = adRepository;
    this.jobRepository = jobRepository;
  }

  public List<Ad> findAll() {
    return this.adRepository.findAll();
  }

  public List<AdDtoView> getAdDtos() {
    return this.adRepository.getAdDtos();
  }

  public Ad findById(String id) {
    return this.adRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("ad", id));
  }

  public List<AdDtoView> getAdDtosByJobId(Long jobId) {
    this.jobRepository.findById(jobId)
        .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

    return this.adRepository.getAdDtosByJobId(jobId);
  }

  public AccountDtoView getAccountDtoByAdId(String adId) {
    this.adRepository.findById(adId)
        .orElseThrow(() -> new ObjectNotFoundException("ad", adId));

    return this.adRepository.getAccountDtoByAdId(adId);
  }

  public long getNumberOfAdsByJobId(long jobId) {
    this.jobRepository.findById(jobId)
        .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

    return this.adRepository.getNumberOfAdsByJobId(jobId);
  }

  public Ad save(Ad ad) {
    return this.adRepository.save(ad);
  }

  public Ad addAd(Long jobId, AdDtoForm adDtoForm) {

    Job foundJob = this.jobRepository.findById(jobId)
        .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

    Ad newAd = new Ad();
    newAd.setHtmlCode(adDtoForm.htmlCode());

    foundJob.addAd(newAd);

    // Dirty check on foundUser, so is automatically persisted

    return this.adRepository.save(newAd);
  }

  public void delete(String adId) {
    Ad foundAd = this.adRepository.findById(adId)
        .orElseThrow(() -> new ObjectNotFoundException("ad", adId));

    this.adRepository.delete(foundAd);
  }
}
