package se.sprinta.headhunterbackend.job.dto;

/**
 * Input Job data format for identifying a job and values that the job should be updated with.
 * Excludes ads field (ManyToOne-relation) to prevent recursion:
 * Instead, numberOfAds represents the amount of ads that the job holds.
 * <p>
 *
 * @param title       The title of the job.
 * @param description The description of the job that is being used by the AI to generate an ad.
 */

public record JobDtoView(
        String title,
        String description,

        String recruiterName,

        String adCompany,

        String adEmail,

        String adPhone,

        String applicationDeadline
) {
}

