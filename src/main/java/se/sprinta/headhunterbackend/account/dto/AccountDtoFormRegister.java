package se.sprinta.headhunterbackend.account.dto;

import jakarta.validation.constraints.NotEmpty;

public record AccountDtoFormRegister(
        @NotEmpty(message = "email is required")
        String email,

        @NotEmpty(message = "password is required")
        String password,

        @NotEmpty(message = "at least one role is required")
        String roles
) {
}
