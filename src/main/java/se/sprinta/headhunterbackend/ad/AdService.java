package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;

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

    public AdService(AdRepository adRepository, JobService jobService, JobRepository jobRepository) {
        this.adRepository = adRepository;
        this.jobService = jobService;
        this.jobRepository = jobRepository;
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
     * @param adId The value used to find the right ad.
     * @return User The object that is the ultimate owner of the ad.
     */

    public User findUserByAdId(String adId) {
        Ad foundAd = findById(adId);
        return foundAd.getJob().getUser();
    }

    public Long getNumberOfAds(Long jobId) {
        return this.adRepository.getNumberOfAds(jobId);
    }


    /**
     * Establishes relationship between job and ad.
     *
     * @param jobId The id that is used to find the job
     * @return Ad The same Ad that was created.
     */

    public Ad addAd(Long jobId, Ad ad) {
        Job foundJob = this.jobRepository.findById(jobId)
                .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

        foundJob.addAd(ad);
        foundJob.setNumberOfAds();
        this.jobService.save(foundJob);

        // TODO: 14/03/2024 Is this really necessary? Should be handled by cascade. 
        ad.setJob(foundJob);

        return this.adRepository.save(ad);
    }

    public void deleteAd(String adId) {
        Ad foundAd = this.adRepository.findById(adId)
                .orElseThrow(() -> new ObjectNotFoundException("ad", adId));

        this.adRepository.delete(foundAd);
    }
}
