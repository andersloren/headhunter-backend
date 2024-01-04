package se.sprinta.headhunterbackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users").permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        // Disallow everything else
                        .anyRequest().authenticated() // Always a good idea to put this as last
                )
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable()) // This is for H2 browser console access
                .csrf(csrf -> csrf.disable()) // If not turned off, there will be problems when sending POST or PUT to server, resulting in 401.
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // TODO: 04/01/2024 testa 31? :)
    }
}
