package com.hs_esslingen.insy.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable()) // disable for testing purposes. Otherwise, POST requests will get blocked
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/**").permitAll() // Disabling all protection for testing purposes
                        .requestMatchers(HttpMethod.PUT, "/**").permitAll() // Disabling all protection for testing purposes
                        .requestMatchers(HttpMethod.PATCH, "/**").permitAll() // Disabling all protection for testing purposes
                        .requestMatchers(HttpMethod.POST, "/**").permitAll() // Disabling all protection for testing purposes
                        .requestMatchers(HttpMethod.DELETE, "/**").permitAll() // Disabling all protection for testing purposes
                        .requestMatchers(HttpMethod.POST, "/upload/csv").permitAll()
                        .requestMatchers(HttpMethod.GET, "/download/xlsx").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://auth.insy.hs-esslingen.com/realms/insy");
    }

}
