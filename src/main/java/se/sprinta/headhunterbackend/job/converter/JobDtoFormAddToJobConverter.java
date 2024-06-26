package se.sprinta.headhunterbackend.job.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;

@Component
public class JobDtoFormAddToJobConverter implements Converter<JobDtoFormAdd, Job> {
    @Override
    public Job convert(JobDtoFormAdd source) {
        Job job = new Job();
        job.setTitle(source.title());
        job.setDescription(source.description());
        job.setInstruction(source.instruction());

        return job;
    }
}
