package se.sprinta.headhunterbackend.job.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;

/**
 * When requesting a Job object, the Job object data needs to be mapped to a JobCardDtoView-object.
 */

@Component
public class JobToJobCardDtoViewConverter implements Converter<Job, JobCardDtoView> {

    /**
     * Converts a given Job to a JobCardDtoView entity
     *
     * @param source Job to convert
     * @return JobCardDtoView Dto object with data from source
     */

    @Override
    public JobCardDtoView convert(Job source) {
        return new JobCardDtoView(
                source.getId(),

                source.getTitle(),

                source.getApplicationDeadline());
    }
}
