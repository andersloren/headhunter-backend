package se.sprinta.headhunterbackend.job.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;

import se.sprinta.headhunterbackend.job.dto.JobDto;


@Component
public class JobToJobDtoConverter
        implements Converter<Job, JobDto> {

    @Override
    public JobDto convert(Job source) {
        return new JobDto(
//                source.getTitle(),
//                source.getUser()
                source.getDescription()
        );
    }
}
