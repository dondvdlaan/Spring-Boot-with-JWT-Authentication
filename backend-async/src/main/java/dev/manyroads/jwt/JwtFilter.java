package dev.manyroads.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Class to verify if the JWT token send in the header of the request is still valid. Subsequently,
 * a new authentication is created, needed to access the endpoint in the controller
 */
@Component
public class JwtFilter implements WebFilter {

    final static Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // Extract JWT Token from request
        String token = parseJwt(exchange.getRequest());
        logger.debug("In filter -> token: " + token);

        // Validate token and if ok, extract userName
        if (token != null) {
            if (jwtUtils.validateJwtToken(token)) {

                String userName = jwtUtils.getUserNameFromJwtToken(token);
                logger.debug("userName: " + userName);

                // This filter needs to create new authentication to access endpoint in controller
                Mono<Void> monoVoid = Mono.fromCallable(() -> jwtUtils.getAuthentication(token))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(authentication -> chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)));

                return monoVoid;

                // Throws 401 unauthorised error when validation went wrong
            } else{
                logger.debug("Token expired, returning UNAUTHORIZED ");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
            }
        }

        return chain.filter(exchange);
    }

    // *** Sub-methods ***

    /**
     * Parse the incoming Authorization header. Return the JWT Token
     *
     * @param request : Incoming request
     * @return String   : JWT Token or null
     */
    private String parseJwt(ServerHttpRequest exchange) {

        String headerAuth = exchange.getHeaders().getFirst("Authorization");

        logger.debug("headerAuth: ", headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
