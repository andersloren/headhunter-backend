package se.sprinta.headhunterbackend.job.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.dto.JobDto;

@Component
public class JobDtoToJobConverter implements Converter<JobDto, Job> {

    @Override
    public Job convert(JobDto source) {
        return new Job(
                source.id(),
                source.description()
        );
    }
}
