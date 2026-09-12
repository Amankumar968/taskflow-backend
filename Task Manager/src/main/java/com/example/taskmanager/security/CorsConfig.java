//package com.example.taskmanager.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//
//import java.util.List;
//
//
//@Configuration
//public class CorsConfig {
//
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource(){
//
//        CorsConfiguration config = new CorsConfiguration();
//
//
//        config.setAllowedOrigins(
//                List.of(
//                        "http://localhost:5173",
//                        "http://localhost:4173"
//                )
//        );
//
//
//        config.setAllowedMethods(
//                List.of(
//                        "GET",
//                        "POST",
//                        "PUT",
//                        "DELETE",
//                        "OPTIONS"
//                )
//        );
//
//
//        config.setAllowedHeaders(
//                List.of(
//                        "Authorization",
//                        "Content-Type"
//                )
//        );
//
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//
//
//        source.registerCorsConfiguration(
//                "/**",
//                config
//        );
//
//
//        return source;
//    }
//}


//package com.example.taskmanager.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//
//import java.util.List;
//
//
//@Configuration
//public class CorsConfig {
//
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource(){
//
//        CorsConfiguration config = new CorsConfiguration();
//
//
//        config.setAllowedOrigins(
//                List.of(
//                        "http://localhost:5173",
//                        "http://localhost:4173",
//                        "https://taskflow-frontend-smoky-psi.vercel.app"
//                )
//        );
//
//
//        config.setAllowedMethods(
//                List.of(
//                        "GET",
//                        "POST",
//                        "PUT",
//                        "DELETE",
//                        "OPTIONS"
//                )
//        );
//
//
//        config.setAllowedHeaders(
//                List.of(
//                        "Authorization",
//                        "Content-Type"
//                )
//        );
//
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//
//
//        source.registerCorsConfiguration(
//                "/**",
//                config
//        );
//
//
//        return source;
//    }
//}


package com.example.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // Uses the CorsConfig bean we defined earlier
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Allow all authentication and phone verification routes
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}