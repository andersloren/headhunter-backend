package se.sprinta.headhunterbackend.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.user.MyUserPrincipal;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.converter.UserToUserDtoViewConverter;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserToUserDtoViewConverter userToUserDtoViewConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoViewConverter userToUserDtoViewConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoViewConverter = userToUserDtoViewConverter;
    }

    /*
     * We retrieve the user info by casting the authentication.getPrincipal() into a MyUserPrincipal object.
     * Then, we can put that user information into a hogwartsUser object.
     *
     * The hogwartsUser object now holds the password, so it must be transformed into a dto.
     * */

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Create user info.
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        UserDtoView userDto = this.userToUserDtoViewConverter.convert(user);

        // Create a JWT.
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
