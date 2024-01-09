package dev.manroads.security;

import dev.manroads.security.jwt.AuthEntryPoint;
import dev.manroads.security.jwt.AuthTokenFilter;
import dev.manroads.security.service.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;


// https://grobmeier.solutions/spring-security-5-jwt-basic-auth.html
//https://medium.com/@2015-2-60-004/multiple-spring-security-configurations-form-based-token-based-authentication-c65ffbeabd07
// https://www.reddit.com/r/java/comments/yzlxcp/any_examples_of_a_well_designed_java_web_backend/

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPoint authEntryPoint;

    public SecurityConfig(AuthTokenFilter authTokenFilter, UserDetailsServiceImpl userDetailsService, AuthEntryPoint authEntryPoint) {
        this.authTokenFilter = authTokenFilter;
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    /**
     * PasswordEncoder to be used by DaoAuthenticationProvider
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Storing PW encode with BCrypt
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configuring DaoAuthenticationProvider bean, an AuthenticationProvider implementation that retrieves
     * user details from a UserDetailsService.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Standard Authentication manager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Spring SecurityFilterChain to set CORS, CRSF, Sessioncookies, authorization rules and JWT authentication filter
     */
    @Bean
    public SecurityFilterChain asFilterChain(HttpSecurity http)
            throws Exception {

        // Configure CORS
            http.cors(c -> {
                CorsConfigurationSource source = request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3005"));
                    config.setAllowedMethods(
                            List.of("GET", "POST", "PUT", "DELETE"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                };
                c.configurationSource(source);
        });
        // Configure CSRF
        //http.csrf(Customizer.withDefaults());
        http.csrf(c -> c.disable());

        // Disbale sessioncookies
        http.sessionManagement(
                s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Set unauthorized request attempt with 401 response by referrring to authEntryPoint
        http.exceptionHandling(e-> e.authenticationEntryPoint(authEntryPoint));

        // Set authorization rules for the endüoints
        http.authorizeHttpRequests(
                a -> a
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/refreshtoken").permitAll()
                        .anyRequest().authenticated());

        // Add JWT authentication filter before regular authentication
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
