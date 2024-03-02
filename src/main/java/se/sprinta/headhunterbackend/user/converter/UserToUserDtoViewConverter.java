package se.sprinta.headhunterbackend.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

@Component
public class UserToUserDtoViewConverter implements Converter<User, UserDtoView> {

    @Override
    public UserDtoView convert(User source) {
        return new UserDtoView(
                source.getEmail(),
                source.getUsername(),
                source.getRoles(),
                source.getNumberOfJobs()
        );
    }
}
