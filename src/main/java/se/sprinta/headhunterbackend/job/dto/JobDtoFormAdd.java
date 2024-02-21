package se.sprinta.headhunterbackend.job.dto;

import jakarta.validation.constraints.NotEmpty;

public record JobDtoFormAdd(
        String email,
        String title,
        @NotEmpty(message = "Description is required.")
        String description,
        String instruction
) {
}
