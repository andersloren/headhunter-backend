package se.sprinta.headhunterbackend.job;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.job.converter.JobDtoFormAddToJobConverter;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.List;

/**
 * Backend API endpoints for Job.
 */

@RestController
@RequestMapping("${api.endpoint.base-url-job}")
@CrossOrigin("http://localhost:5173")
public class JobController {

    private final JobService jobService;
    private final JobToJobDtoViewConverter jobToJobDtoViewConverter;
    private final JobDtoFormAddToJobConverter jobDtoFormAddToJobConverter;

    public JobController(JobService jobService, JobToJobDtoViewConverter jobToJobDtoViewConverter, JobDtoFormAddToJobConverter jobDtoFormAddToJobConverter) {
        this.jobService = jobService;
        this.jobToJobDtoViewConverter = jobToJobDtoViewConverter;
        this.jobDtoFormAddToJobConverter = jobDtoFormAddToJobConverter;
    }

    /**
     * All returned Job objects from jobService are being converted to JobDtoView objects by a stream().
     *
     * @return Result Successful Result object.
     */

    @GetMapping("/findAll")
    public Result findAllJobs() {
        List<Job> foundJobs = this.jobService.findAll();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundJobs);
    }

    @GetMapping("getAllJobDtos")
    public Result getAllJobDtos() {
        List<JobDtoView> foundJobDtos = this.jobService.getAllJobDtos();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundJobDtos);
    }

    /**
     * The user can ask for all Job objects that belong to a User object.
     * All returned Job objects from jobService are being converted to JobDtoView objects by a stream().
     *
     * @param email The id of the User object that holds the Job object that the user is looking for.
     * @return Result Successful Result object.
     */

    @GetMapping("/getAllJobCardsByUserEmail/{email}")
    public Result getAllJobCardDtosByUserEmail(@PathVariable String email) {
        List<JobCardDtoView> userJobs = this.jobService.getAllJobCardsByUserEmail(email);
        return new Result(true, StatusCode.SUCCESS, "Find All Job Id and Titles Dtos Successful", userJobs);
    }

    @GetMapping("/getAllJobDtosByUserEmail/{email}")
    public Result getAllJobsByUserEmail(@PathVariable String email) {
        List<JobDtoView> userJobs = this.jobService.getAllJobDtosByUserEmail(email);
        return new Result(true, StatusCode.SUCCESS, "Find All User Jobs Success", userJobs);
    }

    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable Long id) {
        Job foundJob = this.jobService.findById(id);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", foundJob);
    }

    @GetMapping("/getJobById/{id}")
    public Result getJobById(@PathVariable Long id) {
        JobDtoView JobDtoView = this.jobService.getFullJobDtoByJobId(id);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", JobDtoView);
    }

    @PostMapping("/addJob/{email}")
    public Result addJob(@PathVariable String email, @Valid @RequestBody JobDtoFormAdd jobDtoFormAdd) {
        Job newJob = this.jobDtoFormAddToJobConverter.convert(jobDtoFormAdd);
        Job addedJob = this.jobService.addJob(email, newJob);
        JobDtoView addedJobDtoView = this.jobToJobDtoViewConverter.convert(addedJob);
        return new Result(true, StatusCode.SUCCESS, "Add Success", addedJobDtoView);
    }

    @PutMapping("/update/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody JobDtoFormUpdate update) {
        Job updatedJob = this.jobService.update(id, update);
        JobDtoView updatedJobDto = this.jobToJobDtoViewConverter.convert(updatedJob);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedJobDto);
    }

    /**
     * @param email The id of the User object that holds the Job object that is being deleted
     * @param id    The id of the Job object that is being deleted.
     * @return Result Successful Result object.
     */

    @DeleteMapping("/delete/{email}/{id}")
    public Result delete(@PathVariable String email, @PathVariable Long id) {
        this.jobService.delete(email, id);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    /**
     * This request is being forwarded to an AI API.
     *
     * @param jobId This is the id of the job that the user wants to create an ad for.
     * @return Result Successful Result object.
     */

    @GetMapping("/generate/{jobId}")
    public Result generateAd(@PathVariable Long jobId) {
        String generatedJobAd = this.jobService.generate(jobId);
        return new Result(true, StatusCode.SUCCESS, "Generate Ad Success", generatedJobAd);
    }

    @GetMapping("/getAllJobCardDtosByUserEmail/{email}")
    public Result getJobTitles(@PathVariable String email) {
        List<JobCardDtoView> userJobs = this.jobService.getAllJobCardsByUserEmail(email);
        return new Result(true, StatusCode.SUCCESS, "Find All Job Titles Success", userJobs);
    }
}
