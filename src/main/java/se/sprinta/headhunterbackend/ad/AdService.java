package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.ad.converter.AdDtoFormToAdConverter;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;

import java.util.List;

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

    public User findUserByAdId(String adId) {
        Ad foundAd = findById(adId);
        return foundAd.getJob().getUser();
    }

    /* no save ad as long as Ad doesn't have any children */

    public Ad addAd(Long jobId, Ad ad) {
        Job foundJob = this.jobRepository.findById(jobId)
                .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

//        Job foundJob = this.jobService.findById(jobId);

        foundJob.addAd(ad);
        foundJob.setNumberOfAds();

        this.jobService.save(foundJob);

        return this.adRepository.save(ad);
    }
}
