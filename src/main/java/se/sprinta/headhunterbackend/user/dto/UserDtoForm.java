package se.sprinta.headhunterbackend.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDtoForm(
        @NotEmpty(message = "Username is required.")
        String username,
        @NotEmpty(message = "Roles are required.")
        String roles
) {
}
