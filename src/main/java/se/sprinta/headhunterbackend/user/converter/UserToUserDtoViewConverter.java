package se.sprinta.headhunterbackend.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;


/**
 * When requesting a User object, the User object data needs to be mapped to a UserDtoView object.
 */

@Component
public class UserToUserDtoViewConverter implements Converter<User, UserDtoView> {

    /**
     * Converts a given User to a UserDtoView entity
     *
     * @param source User to convert
     * @return UserDtoView Dto object with data from source
     */
    @Override
    public UserDtoView convert(User source) {
        if (source == null) {
            throw new IllegalStateException("Source in userToUserDtoView.convert() cannot be null");
        }
        return new UserDtoView(
                source.getEmail(),
                source.getRoles(),
                source.getNumberOfJobs()
        );
    }
}
