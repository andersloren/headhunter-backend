package se.sprinta.headhunterbackend.job.dto;

import jakarta.validation.constraints.NotEmpty;
import se.sprinta.headhunterbackend.user.User;

public record JobDtoView(
        Long id,
        String title,
        String description,
        String email,
        String htmlCode,
        String instruction
) {
}

