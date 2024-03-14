package se.sprinta.headhunterbackend.job.dto;

/**
 * Input Job data format for identifying a job and values that the job should be updated with.
 *
 * @param email       The email of the user that holds the job.
 *                    Relationship: [Job] *...1 [User]
 * @param title       The updated title for the job.
 * @param description The updated description of the job that the AI will use to generate an ad.
 * @param instruction The updated instruction that the AI will use to handle the updated description of the Job
 */

// TODO: 14/03/2024 Remove 'email' if it is not being used by the update method.

public record JobDtoFormUpdate(
        String email,
        String title,
        String description,
        String instruction
) {
}
