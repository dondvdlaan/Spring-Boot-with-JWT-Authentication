package dev.manroads.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication filter is set before the regular UsernamePasswordAuthenticationFilter to authenticate users
 * with valid JWT token
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    final static Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT Token from request
        String token = parseJwt(request);
        logger.info("token: " + token);

        try {

            // Validate token and if ok, extract userName
            if (token != null && jwtUtils.validateJwtToken(token)) {

                String userName = jwtUtils.getUserNameFromJwtToken(token);
                logger.info("userName: " + userName);

                // Retrieve UserDetails from userName
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                logger.info("userDetails: " + userDetails.getUsername());

                // Set the authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // coverter of the request in to authentication
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Update SecurityContextHolder with authentication
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Cannot set user authentication: {}", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Parse the incoming Authorization header. Return the JWT Token
     *
     * @param request : Incoming request
     * @return String   : JWT Token or null
     */
    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
