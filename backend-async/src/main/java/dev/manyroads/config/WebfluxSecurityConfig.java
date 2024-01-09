package dev.manyroads.config;

import dev.manyroads.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class WebfluxSecurityConfig {

    ReactiveAuthenticationManager reactiveAuthenticationManager;
    JwtFilter jwtFilter;

    public WebfluxSecurityConfig(ReactiveAuthenticationManager reactiveAuthenticationManager, JwtFilter jwtFilter) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        // Set CORS
        http.cors(c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:3005"));
                config.setAllowedMethods(
                        List.of("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(List.of("*"));
                return config;
            };
            c.configurationSource((org.springframework.web.cors.reactive.CorsConfigurationSource) source);
        });

        http
                .csrf((c -> c.disable()))
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable())
                //.httpBasic(Customizer.withDefaults())
                .authenticationManager(reactiveAuthenticationManager)

                // make our application stateless
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // Set unauthorized request attempt with 401 response by referring to authEntryPoint
                //.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))

                // Set authorization rules
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/login").permitAll()
                        .pathMatchers("/refreshtoken").permitAll()
                        .pathMatchers("/findrefreshtoken").permitAll()
                        .pathMatchers("/test2").authenticated()
                        .anyExchange().authenticated()
                )

                // Set JWT filter before authentication
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
