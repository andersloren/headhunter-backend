package se.sprinta.headhunterbackend.job.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;


@Component
public class JobToJobDtoViewConverter
        implements Converter<Job, JobDtoView> {

    @Override
    public JobDtoView convert(Job source) {
        return new JobDtoView(
                source.getId(),
                source.getDescription(),
                source.getUser().getEmail(),
                source.getHtmlCode()
        );
    }
}
