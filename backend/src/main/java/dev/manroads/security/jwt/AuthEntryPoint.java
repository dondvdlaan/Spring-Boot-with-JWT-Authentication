package dev.manroads.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Class to start an authentication scheme and is implemented in the security filter
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    final static Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);


    /*
    Commences an authentication scheme, but here used merily for catching authentication errors
    Method will be triggerd anytime unauthenticated User requests a secured HTTP resource and
        an AuthenticationException is thrown
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        logger.error("Unauthorized error: ", authException.getMessage());

        //  401 Status code. It indicates that the request requires HTTP authentication.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
