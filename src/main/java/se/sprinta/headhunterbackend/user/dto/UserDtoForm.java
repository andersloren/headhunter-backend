package se.sprinta.headhunterbackend.user.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * Input User data format.
 *
 * @param roles The roles of the User object that authorizes the user to certain parts of the app.
 */

public record UserDtoForm(
        @NotEmpty(message = "Roles are required.")
        String roles
) {
}
