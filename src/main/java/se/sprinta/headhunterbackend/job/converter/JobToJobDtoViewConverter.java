package se.sprinta.headhunterbackend.job.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;

/**
 * When requesting a Job object, the Job object data needs to be mapped to a JobDtoView-object.
 */

@Component
public class JobToJobDtoViewConverter
        implements Converter<Job, JobDtoView> {

    /**
     * Converts a given Job to a JobDtoView entity
     *
     * @param source Job to convert
     * @return JobDtoView Dto object with data from source
     */

    @Override
    public JobDtoView convert(Job source) {
        return new JobDtoView(
                source.getTitle(),
                source.getDescription()
        );
    }
}
