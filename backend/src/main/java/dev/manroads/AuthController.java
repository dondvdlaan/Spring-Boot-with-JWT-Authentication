package dev.manroads;

import dev.manroads.security.jwt.JwtUtils;
import dev.manroads.payload.LoginRequest;
import io.jsonwebtoken.security.WeakKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    final AuthenticationManager authenticationManager;
    final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        logger.info("LoginRequest u: " + loginRequest.getUserName());

        // Authenticating login request
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassWord()));

        logger.info("authentication p: " + authentication.getPrincipal());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Update Secrity context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("SecurityContextHolder: " + SecurityContextHolder.getContext().getAuthentication().getName());


        try {
            // Return jwt token
            return ResponseEntity.ok(jwtUtils.generateJWTToken(authentication));
        } catch (WeakKeyException ex) {
            // Return 500 error
            return ResponseEntity.internalServerError().build();
        }

    }

    // ******************** TESTING *********************
    @PostMapping("/login2")
    public String login2() {

        return "goof";
    }

    @GetMapping("/test")
    public String demo() {

        return "Holita";
    }
    // ******************** END TESTING *********************

}
