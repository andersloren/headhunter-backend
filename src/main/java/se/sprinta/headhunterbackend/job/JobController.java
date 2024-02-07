package se.sprinta.headhunterbackend.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.job.converter.JobDtoToJobConverter;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoConverter;
import se.sprinta.headhunterbackend.job.dto.JobDto;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url-jobs}")
@CrossOrigin("http://localhost:3000")
public class JobController {

    private final JobService jobService;
    private final JobToJobDtoConverter jobToJobDtoConverter;
    private final JobDtoToJobConverter jobDtoToJobConverter;


    public JobController(JobService jobService, JobToJobDtoConverter jobToJobDtoConverter, JobDtoToJobConverter jobDtoToJobConverter) {
        this.jobService = jobService;
        this.jobToJobDtoConverter = jobToJobDtoConverter;
        this.jobDtoToJobConverter = jobDtoToJobConverter;
    }

    @GetMapping("/findAll")
    public Result findAllJobs() {
        List<Job> foundJobs = this.jobService.findAll();
        List<JobDto> foundJobsDtos = foundJobs.stream()
                .map(job -> this.jobToJobDtoConverter.convert(job))
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundJobsDtos);
    }

    @GetMapping("/findJob/{id}")
    public Result findJobById(@PathVariable Long id) {
        Job foundJob = this.jobService.findById(id);
        JobDto jobDto = this.jobToJobDtoConverter.convert(foundJob);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", jobDto);
    }

    @PostMapping("/addJob")
    public Result addJob(@Valid @RequestBody JobDto jobDto) {
        Job job = this.jobDtoToJobConverter.convert(jobDto);
        Job savedJob = this.jobService.save(job);
        JobDto savedJobDto = this.jobToJobDtoConverter.convert(savedJob);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedJobDto);
    }

    @PutMapping("/update/{id}")
    public Result updateJob(@PathVariable Long id, @Valid @RequestBody JobDto update) {
        Job updateJob = this.jobDtoToJobConverter.convert(update);
        Job updatedJob = this.jobService.update(id, updateJob); // TODO: 06/02/2024 fix this at some point
        JobDto updatedJobDto = this.jobToJobDtoConverter.convert(updatedJob);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedJobDto);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteJob(@PathVariable Long id) {
        this.jobService.delete(id);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");

    }

    @GetMapping("/generate/{id}")
    public Result generateJobAd(@PathVariable Long id) throws JsonProcessingException {
        String generatedJobAd = this.jobService.generate(id);
        return new Result(true, StatusCode.SUCCESS, "Summarize Success", generatedJobAd);
    }

}
