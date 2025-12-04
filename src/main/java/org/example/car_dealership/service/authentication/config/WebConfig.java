package org.example.car_dealership.service.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000",
                                "http://192.168.1.33:3000")
                        .allowedMethods("*")
                        .allowedHeaders("*");
                registry.addMapping("/swagger-ui/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("*")
                        .allowedHeaders("*");
                registry.addMapping("/v3/api-docs/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
