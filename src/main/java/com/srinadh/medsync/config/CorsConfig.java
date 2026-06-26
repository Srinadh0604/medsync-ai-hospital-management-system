package com.srinadh.medsync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class CorsConfig {

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration configuration =
//                new CorsConfiguration();
//
//        configuration.addAllowedOrigin("http://127.0.0.1:5500");
//
//        configuration.addAllowedMethod("*");
//
//        configuration.addAllowedHeader("*");
//
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration(
//                "/**",
//                configuration
//        );
//
//        return source;
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(List.of(
//                "http://127.0.0.1:5500",
//                "https://6a3964b775f4259daf12d15e--snazzy-genie-7b38de.netlify.app"
//        ));
//
//        config.setAllowedMethods(List.of(
//                "GET",
//                "POST",
//                "PUT",
//                "DELETE",
//                "OPTIONS"
//        ));
//
//        config.setAllowedHeaders(List.of("*"));
//
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

            CorsConfiguration config = new CorsConfiguration();

            config.setAllowedOriginPatterns(List.of(
                    "http://127.0.0.1:5500",
                    "https://*.netlify.app"
            ));

            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source =
                    new UrlBasedCorsConfigurationSource();

            source.registerCorsConfiguration("/**", config);

            return source;
        }
    }
