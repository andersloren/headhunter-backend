package se.sprinta.headhunterbackend.job.dto;

public record JobDtoFormUpdate(
        String email,
        String description,
        String instruction,
        String htmlCode
) {
}
