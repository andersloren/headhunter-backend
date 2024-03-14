package se.sprinta.headhunterbackend.job.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * Input Job data format.
 *
 * @param email The email of the user that holds the job.
 *              Relationship: [Job] *...1 [User]
 * @param title The title of the job.
 * @param description The description of the job that the AI will use to generate an ad.
 * @param instruction The instruction that the AI will use to handle the description of the Job
 */

public record JobDtoFormAdd(
        String email,
        String title,
        @NotEmpty(message = "Description is required.")
        String description,
        String instruction
) {
}
