package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.ad.converter.AdDtoFormToAdConverter;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.account.Account;

import java.util.List;

/**
 * Business logic for Ad
 */

@Service
@Transactional
public class AdService {

    private final AdRepository adRepository;
    private final JobService jobService;
    private final JobRepository jobRepository;

    private final AdDtoFormToAdConverter adDtoFormToAdConverter;

    public AdService(AdRepository adRepository, JobService jobService, JobRepository jobRepository, AdDtoFormToAdConverter adDtoFormToAdConverter) {
        this.adRepository = adRepository;
        this.jobService = jobService;
        this.jobRepository = jobRepository;
        this.adDtoFormToAdConverter = adDtoFormToAdConverter;
    }

    public List<Ad> findAllAds() {
        return this.adRepository.findAll();
    }

    public Ad findById(String id) {
        return this.adRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("ad", id));
    }

    public List<Ad> findAdsByJobId(Long id) {
        return this.adRepository.findByJob_Id(id);
    }

    /**
     * Returns the user that has a job that holds the ad
     * Relationship: [Ad] *...1 [Job] *...1 [User]
     *
     * @param id The value used to find the right ad.
     * @return User The object that is the ultimate owner of the ad.
     */

    public Account findUserByAdId(String id) {
        Ad foundAd = findById(id);
        return foundAd.getJob().getAccount();
    }

    public long getNumberOfAds(long jobId) {
        return this.adRepository.getNumberOfAds(jobId);
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

    public void deleteAd(String adId) {
        Ad foundAd = this.adRepository.findById(adId)
                .orElseThrow(() -> new ObjectNotFoundException("ad", adId));

        this.adRepository.delete(foundAd);
    }
}
