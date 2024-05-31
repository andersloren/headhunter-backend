package se.sprinta.headhunterbackend.user.dto;

/**
 * Output User data format.
 *
 * @param email          The email of the User object.
 * @param username       The username of the User object.
 * @param roles          The roles of the User object that authorizes the user to certain parts of the app.
 * @param number_of_jobs The number of Job objects that the User object.
 */


public record UserDtoView(
        String email,

        String username,

        String roles,
        int number_of_jobs

) {
}
