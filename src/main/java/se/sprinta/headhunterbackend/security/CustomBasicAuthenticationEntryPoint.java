package se.sprinta.headhunterbackend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * This class handles unsuccessful basic authentication.
 * We implement AuthenticationEntryPoint and then delegate the exception handler to HandlerExceptionResolver.
 */

@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Here we've injected the DefaultHandlerExceptionResolver and delegated the handler to this resolver.
     * This security exception can now be handled with controller advice with an exception handler method.
     */

    private final HandlerExceptionResolver resolver;

    public CustomBasicAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * This class implements the AuthenticationEntryPoint interface
     * and is responsible for handling unsuccessful basic authentication.
     * If a basic authentication fails, this method will get called.
     * We first add a header, and then dedicate the work to the HandlerExceptionResolver.
     * This resolver will resolve the exception so that the exception can be handled by an ExceptionHandlerMethod in the
     * ExceptionHandlerAdvice class.
     **/

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
        this.resolver.resolveException(request, response, null, authException);
    }
}
