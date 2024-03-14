package se.sprinta.headhunterbackend.job.dto;

/**
 * Input Job data format for identifying a job that is to be removed.
 *
 * @param email The email of the user that holds the job. Used for identifying the job.
 *              Relationship: [Job] *...1 [User]
 * @param id The title of the job.
 */

public record JobDtoFormDelete(String email, Long id) {


    // TODO: 14/03/2024 Remove this if it's not being used. Are the parameters sent as @PathVariable:s?
}


