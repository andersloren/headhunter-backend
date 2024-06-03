package se.sprinta.headhunterbackend.UserInfo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.UserInfo.UserInfo;
import se.sprinta.headhunterbackend.UserInfo.dto.UserInfoDtoView;

@Component
public class UserInfoToUserInfoDtoView implements Converter<UserInfo, UserInfoDtoView> {
    @Override
    public UserInfoDtoView convert(UserInfo source) {
        return new UserInfoDtoView(
                source.getName(),
                source.getOrganization()
        );
    }
}
