package se.sprinta.headhunterbackend.job;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job save(Job job) {
        return this.jobRepository.save(job);
    }

    public List<Job> findAll() {
        return this.jobRepository.findAll();
    }

    public Job findById(Long id) {
        return this.jobRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Job Advertisement", id));
    }

    public Job update(Long id, Job jobDetails) {
        Job job = findById(id);
//        jobAdvertisement.setTitle(jobAdvertisementDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
//        jobAdvertisement.setStatus(jobAdvertisementDetails.getStatus());
//        jobAdvertisement.setExpirationDate(jobAdvertisementDetails.getExpirationDate());
        return this.jobRepository.save(job);
    }

    public void delete(Long id) {
        this.jobRepository.deleteById(id);
    }
}
