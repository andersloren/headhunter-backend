package se.sprinta.headhunterbackend.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.dto.UserDto;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        return new UserDto(
                source.getId(),
                source.getUsername()
        );
    }
}
