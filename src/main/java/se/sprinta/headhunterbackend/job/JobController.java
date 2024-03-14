package se.sprinta.headhunterbackend.job;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.List;

/**
 * Backend API endpoints for Job.
 */

@RestController
@RequestMapping("${api.endpoint.base-url-jobs}")
@CrossOrigin("http://localhost:3000")
public class JobController {

    private final JobService jobService;
    private final JobToJobDtoViewConverter jobToJobDtoViewConverter;

    public JobController(JobService jobService, JobToJobDtoViewConverter jobToJobDtoViewConverter) {
        this.jobService = jobService;
        this.jobToJobDtoViewConverter = jobToJobDtoViewConverter;
    }

    /**
     * All returned Job objects from jobService are being converted to JobDtoView objects by a stream().
     *
     * @return Result Successful Result object.
     */

    @GetMapping("/findAll")
    public Result findAllJobs() {
        List<Job> foundJobs = this.jobService.findAll();
        List<JobDtoView> foundJobsDtoView = foundJobs.stream()
                .map(this.jobToJobDtoViewConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundJobsDtoView);
    }

    /**
     * The user can ask for all Job objects that belong to a User object.
     * All returned Job objects from jobService are being converted to JobDtoView objects by a stream().
     *
     * @param email The id of the User object that holds the Job object that the user is looking for.
     * @return Result Successful Result object.
     */

    @GetMapping("/findAllJobsByUserEmail/{email}")
    public Result findAllJobsByUserEmail(@PathVariable String email) {
        List<Job> userJobs = this.jobService.findAllJobsByEmail(email);
        List<JobDtoView> foundUserJobsDtoView = userJobs.stream()
                .map(this.jobToJobDtoViewConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All User Jobs Success", foundUserJobsDtoView);
    }

    @GetMapping("/findJob/{id}")
    public Result findJobById(@PathVariable Long id) {
        Job foundJob = this.jobService.findById(id);
        JobDtoView JobDtoView = this.jobToJobDtoViewConverter.convert(foundJob);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", JobDtoView);
    }

    @PutMapping("/update/{id}")
    public Result updateJob(@PathVariable Long id, @Valid @RequestBody JobDtoFormUpdate update) {
        Job updatedJob = this.jobService.update(id, update); // TODO: 06/02/2024 fix this at some point // // TODO: 14/03/2024 What is that todo talking about?
        JobDtoView updatedJobDto = this.jobToJobDtoViewConverter.convert(updatedJob);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedJobDto);
    }

    /**
     * @param email The id of the User object that holds the Job object that is being deleted
     * @param jobId The id of the Job object that is being deleted.
     * @return Result Successful Result object.
     */

    @DeleteMapping("/delete/{email}/{jobId}")
    public Result deleteJob(@PathVariable String email, @PathVariable Long jobId) {
        this.jobService.delete(email, jobId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PostMapping("/addJob")
    public Result addJob(@Valid @RequestBody JobDtoFormAdd jobDtoFormAdd) {
        Job addedJob = this.jobService.addJob(jobDtoFormAdd);
        JobDtoView addedJobDtoView = this.jobToJobDtoViewConverter.convert(addedJob);
        return new Result(true, StatusCode.SUCCESS, "Add Success", addedJobDtoView);
    }

    /**
     * This request is being forwarded to an AI API.
     *
     * @param documentType This is the specified type of document that the user wants the AI to format its response to.
     * @param jobId        This is the id of the job that the user wants to create an ad for.
     * @return Result Successful Result object.
     */

    @GetMapping("/generate/{documentType}/{jobId}")
    public Result generateJobAd(@PathVariable String documentType, @PathVariable Long jobId) {
        String generatedJobAd = this.jobService.generate(documentType, jobId);
        return new Result(true, StatusCode.SUCCESS, "Summarize Success", generatedJobAd);
    }
}
