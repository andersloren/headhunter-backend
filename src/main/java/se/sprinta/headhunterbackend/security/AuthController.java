package se.sprinta.headhunterbackend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

@RestController
@RequestMapping("${api.endpoint.base-url-account}")
@CrossOrigin(origins = "${CORS_ALLOWED_ORIGIN}"
        , methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE})
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Generate a JSON web token if username and password has been authenticated by the BasicAuthenticationFilter.
     * In summary, this filter is responsible for processing any request that has an HTTP request header of Authorization
     * with an authentication scheme of Basic and a Base64-encoded username:password token.
     * <p>
     * BasicAuthenticationFilter will prepare the Authentication object for this login method.
     * Note: before this login method gets called, Spring Security already authenticated the username and password through Basic Auth.
     * Only successful authentication can make it to this method.
     *
     * @return User information and JSON web token
     */

    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication) {
        LOGGER.debug("Authenticated account: '{}'", authentication.getName());
        return new Result(true, StatusCode.SUCCESS, "Account Info and JSON Web Token", this.authService.createLoginInfo(authentication));
    }
}
