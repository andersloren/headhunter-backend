package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;

import java.util.List;

@Service
@Transactional
public class AdService {

    private final AdRepository adRepository;
    private final JobService jobService;

    public AdService(AdRepository adRepository, JobService jobService) {
        this.adRepository = adRepository;
        this.jobService = jobService;
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


    public Ad saveAd(Ad ad, Long jobId) {
        Job foundJob = this.jobService.findById(jobId);
        ad.setJob(foundJob);
        foundJob.addAd(ad);
        this.jobService.save(foundJob);
        return this.adRepository.save(ad);
    }
}
