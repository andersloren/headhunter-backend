package se.sprinta.headhunterbackend.job.sandbox;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;

@Component
public class JobServiceSandbox {

    JobRepository jobRepository;

    public JobServiceSandbox(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createNewJob(JobDtoFormAdd jobDtoFormAdd) {
        Job newJob = new Job();
        newJob.setTitle(jobDtoFormAdd.title());
        return this.jobRepository.save(newJob);
    }
}
