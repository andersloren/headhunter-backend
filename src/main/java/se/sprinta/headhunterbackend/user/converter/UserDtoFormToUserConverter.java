package se.sprinta.headhunterbackend.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.dto.UserDtoForm;

/**
 * User data needs to be mapped to User entity format.
 */

@Component
public class UserDtoFormToUserConverter implements Converter<UserDtoForm, User> {
    /**
     * Converts a given UserDtoForm to a User entity
     *
     * @param source UserDtoForm to convert
     * @return User entity with data from source
     */


    @Override
    public User convert(UserDtoForm source) {
        return new User(source.username(), source.roles());
    }
}
