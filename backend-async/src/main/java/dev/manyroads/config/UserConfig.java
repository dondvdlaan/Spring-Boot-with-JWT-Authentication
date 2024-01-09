package dev.manyroads.config;

import dev.manyroads.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    UserDetailsServiceImpl userDetailsService;

    public UserConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

   /*
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
        //UserDetails user = User.withDefaultPasswordEncoder()
                .withUsername("user")
                .password("user")
                .roles("USER")
                .build();
        
        return new MapReactiveUserDetailsService(user);
    }
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Storing PW encode with BCrypt
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {

        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        authenticationManager.setPasswordEncoder(passwordEncoder());

        return authenticationManager;
    }

}
