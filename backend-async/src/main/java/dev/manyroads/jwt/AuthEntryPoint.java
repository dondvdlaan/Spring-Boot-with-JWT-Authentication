package dev.manyroads.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * Class to start an authentication scheme and is implemented in the security filter
 * !!! CURRENTLY NOT ISED !!!
 */
@Component
public class AuthEntryPoint implements ServerAuthenticationEntryPoint {

    final static Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return null;
    }


    /*
    Commences an authentication scheme, but here used merily for catching authentication errors
    Method will be triggerd anytime unauthenticated User requests a secured HTTP resource and
        an AuthenticationException is thrown

    @Override
    public Mono<void> commence(
            ServerWebExchange exchange,
            AuthenticationException authException)
            throws IOException, ServerErrorException {

        logger.error("Unauthorized error: ", authException.getMessage());

        ServerWebRHttpResponse response = exchange.getResponse();
        //  401 Status code. It indicates that the request requires HTTP authentication.
        return exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
    }
*/

}
