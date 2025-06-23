package com.hs_esslingen.insy.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@Profile("dev") // Activate this configuration only in the "dev" profile
public class SecurityConfigDev {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOrigin("http://localhost:4200"); // Allow local development origin
                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
                    corsConfiguration.addAllowedHeader("*");
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("SYSTEM") // Only allow BeSy to create
                                                                                          // orders
                        .requestMatchers(HttpMethod.GET, "/**").permitAll() // Disabling all protection for testing
                                                                            // purposes
                        .requestMatchers(HttpMethod.PUT, "/**").permitAll() // Disabling all protection for testing
                                                                            // purposes
                        .requestMatchers(HttpMethod.PATCH, "/**").permitAll() // Disabling all protection for testing
                                                                              // purposes
                        .requestMatchers(HttpMethod.POST, "/**").permitAll() // Disabling all protection for testing
                                                                             // purposes
                        .requestMatchers(HttpMethod.DELETE, "/**").permitAll() // Disabling all protection for testing
                                                                               // purposes
                        .requestMatchers(HttpMethod.POST, "/upload/csv").permitAll()
                        .requestMatchers(HttpMethod.GET, "/download/xls").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()) // Enable HTTP Basic authentication for BeSy-API
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://auth.insy.hs-esslingen.com/realms/insy");
    }

    // Hardcoded user to allow BeSy to access the API
    // Uses basic authentication
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("besy")
                .password(encoder.encode("secret"))
                .roles("SYSTEM")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Password encoder bean for encoding passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
