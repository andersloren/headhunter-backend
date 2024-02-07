package se.sprinta.headhunterbackend.job.dto;

import jakarta.validation.constraints.NotEmpty;

public record JobDtoForm(
        String email,
        @NotEmpty(message = "Description is required.")
        String description
) {
}
