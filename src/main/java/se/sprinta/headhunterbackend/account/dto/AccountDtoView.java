package se.sprinta.headhunterbackend.account.dto;

/**
 * Output User data format.
 *
 * @param email          The email of the User object.
 * @param roles          The roles of the User object that authorizes the user to certain parts of the app.
 */


public record AccountDtoView(
        String email,

        String roles,

        long number_of_jobs

) {
}
