package se.sprinta.headhunterbackend.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.dto.UserDtoForm;

@Component
public class UserDtoFormToUserConverter implements Converter<UserDtoForm, User> {
    @Override
    public User convert(UserDtoForm source) {
        return new User(source.username(), source.roles());
    }
}
