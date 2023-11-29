package dev.manroads.controllers;

import dev.manroads.payload.JwtResponse;
import dev.manroads.payload.TokenRefreshRequest;
import dev.manroads.security.jwt.JwtUtils;
import dev.manroads.payload.LoginRequest;
import dev.manroads.security.jwt.models.RefreshToken;
import dev.manroads.security.service.RefreshTokenService;
import io.jsonwebtoken.security.WeakKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for authenticating the user
 */
@RestController
public class AuthController {

    final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Method to authenticate user based on username/password and responds with JWT and refreshToken
     *
     * @param loginRequest : username/password input
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        logger.info("LoginRequest u: " + loginRequest.getUserName());

        // Authenticating login request
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassWord()));

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
        String jwtToken = jwtUtils.generateJWTToken(userDetails);

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);

        // Compose Jwt response
        JwtResponse jwtResponse = new JwtResponse(jwtToken,refreshToken.getToken(),userDetails.getUsername(),roles);

        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Method to return a new JWT token after a request with a refreshToken
     *
     * @return
     */
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshJWTToken(@RequestBody TokenRefreshRequest tokenRefreshRequest ){

        logger.info("In refreshJWTToken: " + tokenRefreshRequest.getRefreshToken());
        // Check if refersh token has not expired


        // Generates JWT token
        String jwtToken = jwtUtils.generateJWTFromUsername("test");

        // Generate refresh token
        //RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);

        // Compose Jwt response
        JwtResponse jwtResponse = new JwtResponse(
                jwtToken,
                "aaaaaaaaaa",
                "test",
                List.of("ROLE_USER"));

        return ResponseEntity.ok(jwtResponse);
        //return ResponseEntity.badRequest().build();

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
