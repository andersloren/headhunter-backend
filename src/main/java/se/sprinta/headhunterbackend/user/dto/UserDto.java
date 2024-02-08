package se.sprinta.headhunterbackend.user.dto;

import jakarta.validation.constraints.NotEmpty;
import se.sprinta.headhunterbackend.job.Job;

import java.util.List;

public record UserDto(@NotEmpty(message = "email is required.")
                      String email,

                      @NotEmpty(message = "username is required.")
                      String username,

                      String roles

) {
}
