package com.hs_esslingen.insy.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Klasse zur Konfiguration von CORS (Cross-Origin Resource Sharing)
// Setzt die erlaubten Ursprünge, Methoden und Header für CORS-Anfragen
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Frontend-Origin
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*");
        // .allowCredentials(true);
    }
}
