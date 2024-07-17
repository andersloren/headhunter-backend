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

    public List<AdDtoView> getAllAdDtos() {
        return this.adRepository.getAllAdDtos();
    }

    public Ad findById(String id) {
        return this.adRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("ad", id));
    }

    public List<Ad> getAdsByJobId(long jobId) {
        this.jobRepository.findById(jobId)
                .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

        return this.adRepository.getAdsByJobId(jobId);
    }

    public List<AdDtoView> getAdDtosByJobId(Long jobId) {
        this.jobRepository.findById(jobId)
                .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

        return this.adRepository.getAdDtosByJobId(jobId);
    }

    /**
     * Returns the user that has a job that holds the ad
     * Relationship: [Ad] *...1 [Job] *...1 [User]
     *
     * @param adId The value used to find the right ad.
     * @return Account The object that is the ultimate owner of the ad.
     */

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

    /**
     * Establishes relationship between job and ad.
     *
     * @param jobId The id that is used to find the job
     * @return Ad The same Ad that was created.
     */

    public Ad addAd(Long jobId, AdDtoForm adDtoForm) {
        if (adDtoForm == null) throw new NullPointerException("Ad can't be null");

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
