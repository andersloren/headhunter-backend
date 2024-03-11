package se.sprinta.headhunterbackend.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDtoView(
        String email,

        String username,

        String roles,
        int numberOfJobs

) {
}
