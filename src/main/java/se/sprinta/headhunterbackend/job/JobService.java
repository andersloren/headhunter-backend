package se.sprinta.headhunterbackend.job;


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
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));
    }

    public Job update(Long id, Job update) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));
//        job.setTitle(update.getTitle());
        job.setDescription(update.getDescription());
//        job.setStatus(update.getStatus());
//        job.setExpirationDate(update.getExpirationDate());
        return this.jobRepository.save(job);
    }

    public void delete(Long id) {
        Job foundJob = this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));
        this.jobRepository.delete(foundJob);
    }
}
