package se.sprinta.headhunterbackend.job.dto;

public record JobDtoFormUpdate(
        String email,
        String title,
        String description,
        String instruction
) {
}
