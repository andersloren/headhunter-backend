package se.sprinta.headhunterbackend.job;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormRemove;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.List;

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

    @GetMapping("/findAll")
    public Result findAllJobs() {
        List<Job> foundJobs = this.jobService.findAll();
        List<JobDtoView> foundJobsDtoView = foundJobs.stream()
                .map(job -> this.jobToJobDtoViewConverter.convert(job))
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundJobsDtoView);
    }

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
        System.out.println("GOT TO CONTROLLER!!!!!!!!!!!!!!!!!");
        Job updatedJob = this.jobService.update(id, update); // TODO: 06/02/2024 fix this at some point
        JobDtoView updatedJobDto = this.jobToJobDtoViewConverter.convert(updatedJob);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedJobDto);
    }

    @DeleteMapping("/delete")
    public Result deleteJob(@RequestBody JobDtoFormRemove jobDtoFormRemove) {
        this.jobService.delete(jobDtoFormRemove);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PostMapping("/addJob")
    public Result addJob(@Valid @RequestBody JobDtoFormAdd jobDtoFormAdd) {
//        Job newJob = this.jobDtoForm
        Job addedJob = this.jobService.addJob(jobDtoFormAdd);
        JobDtoView addedJobDtoView = this.jobToJobDtoViewConverter.convert(addedJob);
        return new Result(true, StatusCode.SUCCESS, "Add Success", addedJobDtoView);
    }

    @GetMapping("/generate/{id}")
    public Result generateJobAd(@PathVariable Long id) {
        String generatedJobAd = this.jobService.generate(id);
        return new Result(true, StatusCode.SUCCESS, "Summarize Success", generatedJobAd);
    }
}
