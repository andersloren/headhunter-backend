package se.sprinta.headhunterbackend.job.dto;

/**
 * Input Job data format for identifying a job and values that the job should be updated with.
 * Excludes ads field (ManyToOne-relation) to prevent recursion:
 *      Instead, numberOfAds represents the amount of ads that the job holds.
 *
 * @param id The id of the job.
 * @param title The title of the job.
 * @param description The description of the job that is being used by the AI to generate an ad.
 * @param email The email of the user that holds the job.
 *              Relationship: [Job] *...1 [User]
 * @param instruction The instruction of the job that is being used by the AI to handle the description of the Job in order to create an ad.
 * @param numberOfAds The number of Ad objects that the Job object holds.
 */

public record JobDtoView(
        Long id,
        String title,
        String description,
        String email,
        String instruction,
        int numberOfAds
) {
}

