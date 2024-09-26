package se.sprinta.headhunterbackend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.account.MyAccountPrincipal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiresIn = 1; // 1 hour

        // Prepare a claim called authorities.
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Inverse of what we did in MyAccountPrincipal
                .collect(Collectors.joining(" ")); // Space delimiter as decided earlier

        String email = ((MyAccountPrincipal) authentication.getPrincipal()).getName();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(expiresIn, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("roles", authorities) // The account's roles (user, admin)
                .claim("email", email) // This account's email
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
