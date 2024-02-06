package se.sprinta.headhunterbackend.job.dto;

import jakarta.validation.constraints.NotEmpty;


public record JobDto(
//        @NotEmpty(message = "Title is required.")
//        String title,

        @NotEmpty(message = "Description is required.")
        String description
//        @NotEmpty(message = "User is required.")
//        User user
) {
}

