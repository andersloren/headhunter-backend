package se.sprinta.headhunterbackend.account.dto;

import java.util.Objects;

/**
 * Output User data format.
 *
 * @param email The email of the User object.
 * @param roles The roles of the User object that authorizes the user to certain parts of the app.
 */


public record AccountDtoView(
        String email,

        String roles,

        long number_of_jobs,

        boolean isVerified

) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDtoView that = (AccountDtoView) o;
        return number_of_jobs == that.number_of_jobs && Objects.equals(email, that.email) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, roles, number_of_jobs);
    }
}


