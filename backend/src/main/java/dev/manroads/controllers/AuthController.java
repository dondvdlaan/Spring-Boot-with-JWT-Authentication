package dev.manroads.controllers;

import dev.manroads.payload.JwtResponse;
import dev.manroads.payload.TokenRefreshRequest;
import dev.manroads.repositories.RefreshTokenRepository;
import dev.manroads.security.jwt.JwtUtils;
import dev.manroads.payload.LoginRequest;
import dev.manroads.security.jwt.models.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class for authenticating the user
 */
@RestController
@CrossOrigin
public class AuthController {

    final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final int jwtExpirationMs;
    private final int refreshExpirationMs;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;



    public AuthController(
            @Value("${jwtExpirationMs}") int jwtExpirationMs,
            @Value("${refreshExpirationMs}") int refreshExpirationMs,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            RefreshTokenRepository refreshTokenRepository) {

        this.jwtExpirationMs = jwtExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Method to authenticate user based on username/password and responds with JWT and refreshToken
     *
     * @param loginRequest : username/password input
     * @return
     */
    @PostMapping("/login")

    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res) {

        logger.info("LoginRequest u: " + loginRequest.getUserName() + " " + loginRequest.getPassWord());

        // Authenticating login request. AuthenticationManager uses DaoAuthenticationProvider
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassWord()));

        logger.info("authentication: " + authentication.getName() + " " + authentication.isAuthenticated());

        // Update Security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Extract UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Extract roles from Userdetails
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(i -> i.getAuthority())
                .collect(Collectors.toList());

        // Generates JWT token
        String jwtToken = jwtUtils.generateJWTToken(userDetails, jwtExpirationMs);

        // Generate refresh token
        String refreshToken = jwtUtils.generateJWTToken(userDetails, refreshExpirationMs);
        //String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        refreshTokenRepository.save(new RefreshToken(refreshToken));

        // Compose Jwt response
        JwtResponse jwtResponse = new JwtResponse(
                jwtToken,
                refreshToken,
                userDetails.getUsername(),
                roles);

       return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Method to return a new JWT token, along with a new refreshToken, after checking the request
     * for a valid refreshToken.
     *
     * @return
     */
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshJWTToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {

        logger.info("In refreshJWTToken: " + tokenRefreshRequest.getRefreshToken());

        // Check if refresh token exists in DB
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());

        if (optionalRefreshToken.isPresent()) {

           RefreshToken refreshToken = optionalRefreshToken.get();

            // Check if refresh token is still valid
            if (jwtUtils.validateJwtToken(refreshToken.getRefreshToken())) {

                // Retrieve user name
                String userName = jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken());

                // Generates new JWT token
                String jwtToken = jwtUtils.generateJWTFromUsername(userName, jwtExpirationMs);

                // Generate new refresh token
                String newRefreshToken = jwtUtils.generateJWTFromUsername(userName, refreshExpirationMs);

                // Delete old refresh token and save new refresh token
                refreshTokenRepository.delete(refreshToken);
                refreshTokenRepository.save(new RefreshToken(newRefreshToken));

                // Compose Jwt response
                JwtResponse jwtResponse = new JwtResponse(
                        jwtToken,
                        newRefreshToken,
                        userName,
                        List.of("ROLE_USER"));

                return ResponseEntity.ok(jwtResponse);
            }
        }
        // Refreshtoken missing or invalid
        return ResponseEntity.badRequest().build();
    }


    // ******************** TESTING *********************
    @PostMapping("/login2")
    public String login2() {

        return "goof";
    }

    @GetMapping("/test")
    public String demo() {

        return "Holita" + new Date().getSeconds();
    }
    // ******************** END TESTING *********************

}
