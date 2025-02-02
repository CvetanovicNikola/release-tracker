package com.example.release_tracker_gateway.neon_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/releases/welcome", "/logout").permitAll()
                        .pathMatchers(HttpMethod.GET,"/releases/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/releases/**").hasRole("ADMIN")
                        .anyExchange().hasRole("ADMIN")

                )
                .httpBasic(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) -> {
                            return exchange.getExchange().getSession()
                                    .flatMap(session -> {
                                        session.invalidate();
                                        return exchange.getExchange().getResponse()
                                                .writeWith(Mono.just(exchange.getExchange().getResponse()
                                                        .bufferFactory().wrap("{\"message\":\"Logout successful\"}".getBytes())));
                                    });
                        })
                )
                .csrf().disable()
                .build();
    }
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        var user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .build();

        var admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
