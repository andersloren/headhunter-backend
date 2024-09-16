package se.sprinta.headhunterbackend.job;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.job.converter.JobDtoFormAddToJobConverter;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import jakarta.validation.Valid;

/**
 * Backend API endpoints for Job.
 */

@RestController
@RequestMapping("${api.endpoint.base-url-job}")
@CrossOrigin(origins = "${CORS_ALLOWED_ORIGIN}"
        , methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE})
public class JobController {

  private final JobService jobService;
  private final JobToJobDtoViewConverter jobToJobDtoViewConverter;
  private final JobDtoFormAddToJobConverter jobDtoFormAddToJobConverter;

  public JobController(JobService jobService, JobToJobDtoViewConverter jobToJobDtoViewConverter,
      JobDtoFormAddToJobConverter jobDtoFormAddToJobConverter) {
    this.jobService = jobService;
    this.jobToJobDtoViewConverter = jobToJobDtoViewConverter;
    this.jobDtoFormAddToJobConverter = jobDtoFormAddToJobConverter;
  }

  /**
   * All returned Job objects from jobService are being converted to JobDtoView
   * objects by a stream().
   *
   * @return Result Successful Result object.
   */

  @GetMapping("/findAll")
  public Result findAllJobs() {
    List<Job> foundJobs = this.jobService.findAll();
    return new Result(true, StatusCode.SUCCESS, "Find All Jobs Success", foundJobs);
  }

  @GetMapping("getJobDtos")
  public Result getAllJobDtos() {
    List<JobDtoView> foundJobDtos = this.jobService.getJobDtos();
    return new Result(true, StatusCode.SUCCESS, "Find Job Dtos Success", foundJobDtos);
  }

  @GetMapping("/getJobCardDtosByEmail/{email}")
  public Result getAllJobCardDtosByEmail(@PathVariable String email) {
    List<JobCardDtoView> userJobs = this.jobService.getJobCardDtosByEmail(email);
    return new Result(true, StatusCode.SUCCESS, "Find Job Cards By User Email Successful", userJobs);
  }

  @GetMapping("/getJobDtosByEmail/{email}")
  public Result getJobDtosByEmail(@PathVariable String email) {
    List<JobDtoView> userJobs = this.jobService.getJobDtosByEmail(email);
    return new Result(true, StatusCode.SUCCESS, "Find User Job Dtos Success", userJobs);
  }

  @GetMapping("/findById/{jobId}")
  public Result findById(@PathVariable Long jobId) {
    Job foundJob = this.jobService.findById(jobId);
    return new Result(true, StatusCode.SUCCESS, "Find One Success", foundJob);
  }

  @GetMapping("/getJobDto/{id}")
  public Result getJobDto(@PathVariable Long id) {
    JobDtoView JobDtoView = this.jobService.getJobDto(id);
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
   * @param email The id of the User object that holds the Job object that is
   *              being deleted
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
   * @param jobId This is the id of the job that the user wants to create an ad
   *              for.
   * @return Result Successful Result object.
   */

  @GetMapping("/generate/{jobId}")
  public Result generateAd(@PathVariable Long jobId) {
    String generatedJobAd = this.jobService.generate(jobId);
    return new Result(true, StatusCode.SUCCESS, "Generate Ad Success", generatedJobAd);
  }
}
