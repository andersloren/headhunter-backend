package se.sprinta.headhunterbackend.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(String id,

                      @NotEmpty(message = "username is required.")
                      String username
) {
}
