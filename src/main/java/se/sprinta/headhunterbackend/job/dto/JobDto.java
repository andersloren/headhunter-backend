package se.sprinta.headhunterbackend.job.dto;

import jakarta.validation.constraints.NotEmpty;

public record JobDto(

        Long id,

        @NotEmpty(message = "Description is required.")
        String description
) {
}

