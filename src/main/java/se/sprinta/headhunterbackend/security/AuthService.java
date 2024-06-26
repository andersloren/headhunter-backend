package se.sprinta.headhunterbackend.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.MyAccountPrincipal;
import se.sprinta.headhunterbackend.account.converter.AccountToAccountDtoViewConverter;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AccountToAccountDtoViewConverter accountToAccountDtoViewConverter;

    public AuthService(JwtProvider jwtProvider, AccountToAccountDtoViewConverter accountToAccountDtoViewConverter) {
        this.jwtProvider = jwtProvider;
        this.accountToAccountDtoViewConverter = accountToAccountDtoViewConverter;
    }

    /**
     * We retrieve the user info by casting the authentication.getPrincipal() into a MyUserPrincipal object.
     * Then, we can put that user information into a User object.
     * <p>
     * The User object now holds the password, so it must be transformed into a dto.
     */

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Create user info.
        MyAccountPrincipal principal = (MyAccountPrincipal) authentication.getPrincipal();
        Account account = principal.getUser();
        AccountDtoView accountDtoView = this.accountToAccountDtoViewConverter.convert(account);

        // Create a JWT.
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("accountInfo", accountDtoView);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
