package se.sprinta.headhunterbackend.user.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * Input User data format.
 *
 * @param username The username of the User object.
 * @param roles The roles of the User object that authorizes the user to certain parts of the app.
 */

public record UserDtoForm(
        @NotEmpty(message = "Username is required.")
        String username,
        @NotEmpty(message = "Roles are required.")
        String roles
) {
}
