package se.sprinta.headhunterbackend.account.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * Input User data format.
 *
 * @param roles The roles of the User object that authorizes the user to certain parts of the app.
 */

public record AccountDtoFormUpdate(
        @NotEmpty(message = "Roles are required.")
        String roles
) {
}
