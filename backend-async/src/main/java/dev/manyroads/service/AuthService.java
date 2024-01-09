package dev.manyroads.service;

import dev.manyroads.exceptions.NotFoundException;
import dev.manyroads.jwt.JwtUtils;
import dev.manyroads.models.RefreshToken;
import dev.manyroads.payloads.JwtResponse;
import dev.manyroads.payloads.LoginRequest;
import dev.manyroads.payloads.TokenRefreshRequest;
import dev.manyroads.repositories.RefreshTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static java.util.logging.Level.FINE;

/*
Class to offer different methods to the authentication controller
 */
@Service
public class AuthService {

    Logger logger = LogManager.getLogger(AuthService.class);

    private final int jwtExpirationMs;
    private final int refreshExpirationMs;
    ReactiveAuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    RefreshTokenRepository refreshTokenRepository;
    private final Scheduler jdbcScheduler;

    public AuthService(
            @Value("${jwtExpirationMs}") int jwtExpirationMs,
            @Value("${refreshExpirationMs}") int refreshExpirationMs,
            ReactiveAuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            RefreshTokenRepository refreshTokenRepository,
            Scheduler jdbcScheduler) {
        this.jwtExpirationMs = jwtExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    /*
    Method to authenticate the login request
     */
    public Mono<ResponseEntity> authenticateLogin(LoginRequest loginRequest) {

        logger.info("authenticateLogin");
        logger.debug(loginRequest);

        // Authenticate login request
        Mono<Authentication> monoAuth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.userName(), loginRequest.passWord()))
                // If not authenticated, return 401 Error Unauthorized (originally this error throws 500 int serv err)
                .doOnError(ex -> {
                    logger.warn("Authentication failed: {}", ex.getMessage());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
                });

        // Create jwt token
        Mono<String> monoJwt = monoAuth.map(a -> jwtUtils.generateJWT(a.getName(), jwtExpirationMs));

        // Create refresh token and save to DB
        Mono<RefreshToken> monoRefreshToken = monoAuth.map(a -> jwtUtils.generateJWT(a.getName(), refreshExpirationMs))
                .map(j -> new RefreshToken(j))
                .map(rt -> refreshTokenRepository.save(rt));

        // Compose Jwt response
        Mono<JwtResponse> MonoJwtResponse = Mono.zip(monoJwt, monoRefreshToken)
                .map(monoZip -> new JwtResponse(monoZip.getT1(), monoZip.getT2().getRefreshToken()));

        // return ResponseEntity
        return MonoJwtResponse.map(jr -> new ResponseEntity<>(jr, null, HttpStatus.OK));
    }

    /*
    Method to handle the token refresh request
    */
    public Mono<ResponseEntity<JwtResponse>> handleTokenRefresh(TokenRefreshRequest tokenRefreshRequest) {

        logger.info("handleTokenRefresh");
        logger.debug(tokenRefreshRequest);

        logger.debug("Check if refresh token exists in DB");

        // Check if refresh token exists in DB
        Mono<RefreshToken> monoRTRetrieved =
                Mono.fromCallable(() -> refreshTokenRepository.findByRefreshToken(tokenRefreshRequest.refreshToken()))
                        // unchecked exception, not to be caught
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN)))
                        .subscribeOn(jdbcScheduler);

        logger.debug("Check if refresh token is still valid, if not, respond with 403 forbidden error");

        // Check if refresh token is still valid, if not, respond with 403 forbidden error
        Mono<RefreshToken> monoRTValidated = monoRTRetrieved.map(rtr -> {
            try {
                jwtUtils.validateJwtToken(rtr.getRefreshToken());
            } catch (Exception e) {

                logger.debug("Throwing 403 forbidden error");

                new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            return rtr;
        });

        // Extract user name
        Mono<String> monoUserName = monoRTValidated.map(rtv -> jwtUtils.getUserNameFromJwtToken(rtv.getRefreshToken()));

        // Create new jwt token
        Mono<String> monoJwt = monoUserName.map(u -> jwtUtils.generateJWT(u, jwtExpirationMs));

        logger.debug("Create new refresh token and save to DB");

        // Create new refresh token and save to DB
        Mono<RefreshToken> monoRefreshToken = monoUserName.map(u -> jwtUtils.generateJWT(u, refreshExpirationMs))
                .map(j -> new RefreshToken(j))
                .map(rt -> refreshTokenRepository.save(rt))
                .subscribeOn(jdbcScheduler);

        logger.debug("Delete old refresh token");

        // Delete old refresh token
        refreshTokenRepository.delete(new RefreshToken(tokenRefreshRequest.refreshToken()));

        // Compose Jwt response
        Mono<JwtResponse> monoJwtResponse = Mono.zip(monoJwt, monoRefreshToken)
                .map(monoZip -> new JwtResponse(monoZip.getT1(), monoZip.getT2().getRefreshToken()));

        return monoJwtResponse.map(js -> new ResponseEntity(js, null, HttpStatus.OK));
    }

    // *** TESTING ***
    public Mono<ResponseEntity> findRefreshToken(String refreshToken) {

        logger.info("findTokenRefresh");
        logger.debug(refreshToken);

        Mono<RefreshToken> monoRTRetrieved =
                Mono.fromCallable(() -> refreshTokenRepository.findByRefreshToken(refreshToken))
                        // unchecked exception, not to be caught
                        .switchIfEmpty(Mono.error(new NotFoundException("No token found for: " + refreshToken)))
                        .subscribeOn(jdbcScheduler);

        return monoRTRetrieved.map(rtr ->
                new ResponseEntity(rtr.getRefreshTokenID(), null, HttpStatus.OK));
    }
    // *** END TESTING ***
}
