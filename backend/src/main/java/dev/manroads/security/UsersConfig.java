package dev.manroads.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UsersConfig {

    @Bean
    public UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {

        UserDetails testUser = User
                .withUsername("test")
                .password(passwordEncoder.encode("testPW"))
                .roles("USER") // No roles for now
                .build();

        UserDetails testUser2 = User
                .withUsername("test2")
                .password(passwordEncoder.encode("testPW2"))
                .roles("USER2") // No roles for now
                .build();

        return new InMemoryUserDetailsManager(testUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Storing PW encode with BCrypt
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }
}
