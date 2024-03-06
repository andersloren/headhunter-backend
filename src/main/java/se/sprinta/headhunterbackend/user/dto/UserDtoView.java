package se.sprinta.headhunterbackend.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDtoView(@NotEmpty(message = "email is required.")
                          String email,

                          @NotEmpty(message = "username is required.")
                          String username,

                          String roles,
                          int numberOfJobs

) {
}
