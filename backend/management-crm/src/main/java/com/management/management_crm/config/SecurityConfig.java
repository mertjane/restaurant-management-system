/* package com.management.management_crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource; // Inject the bean from CorsConfig

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Use the injected CORS config
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/auth.html", "/email-sent-success.html", "/dashboard.html",
                                "/password-reset-success.html", "/reset-password.html", "/auth/register", "/auth/login", "/dashboard",
                                "/auth/forgot-password","/users", "/auth/reset-password", "/css/**",
                                "/js/**", "/images/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/reset-password").permitAll() // Explicitly allow GET
                        .requestMatchers(HttpMethod.POST, "/reset-password").permitAll() // Explicitly allow POST
                        .requestMatchers("/frontend/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
} */

/* package com.management.management_crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // Single allowed origin
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.PATCH);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedMethod(HttpMethod.OPTIONS); // Allow preflight requests
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.setAllowCredentials(true); // Allow credentials (cookies, etc.)
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use the defined CORS config
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/analytics-customers/**").permitAll()
                        .requestMatchers("/analytics/**").permitAll()
                        .requestMatchers("/customers/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/restaurant/**").permitAll()
                        .requestMatchers("/bookings/**").permitAll()
                        .requestMatchers("/menu/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/analytics/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/bookings/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/restaurant/**").permitAll()
                        .requestMatchers("/auth/**", "/auth.html", "/email-sent-success.html", "/dashboard.html",
                                "/password-reset-success.html", "/reset-password.html", "/auth/register", "/auth/login",
                                "/dashboard", "/auth/forgot-password", "/users", "/customers", "/restaurant",
                                "/auth/reset-password", "/css/**",
                                "/js/**", "/images/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/reset-password").permitAll() // Explicitly allow GET
                        .requestMatchers(HttpMethod.POST, "/auth/reset-password").permitAll() // Explicitly allow POST
                        .requestMatchers("/frontend/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
} */

package com.management.management_crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String API_PREFIX = "/api/v1";

    // Public API endpoints
    private static final String[] PUBLIC_API_ENDPOINTS = {
            API_PREFIX + "/auth/**",
            API_PREFIX + "/analytics/**",
            API_PREFIX + "/analytics-customers/**",
            API_PREFIX + "/customers/**",
            API_PREFIX + "/restaurant/**",
            API_PREFIX + "/bookings/**",
            API_PREFIX + "/orders/**",
            API_PREFIX + "/menu/**"
    };

    // Public static resources
    private static final String[] PUBLIC_RESOURCES = {
            "/css/**",
            "/js/**",
            "/images/**",
            "/frontend/**",
            "/*.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration apiConfig = new CorsConfiguration();
        apiConfig.setAllowedOrigins(List.of("http://localhost:5173"));
        apiConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        apiConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        apiConfig.setExposedHeaders(List.of("Authorization"));
        apiConfig.setAllowCredentials(true);
        apiConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply CORS to all API endpoints
        source.registerCorsConfiguration("/api/**", apiConfig);
        
        // Public resources config
        CorsConfiguration publicConfig = new CorsConfiguration();
        publicConfig.setAllowedOrigins(List.of("*"));
        publicConfig.setAllowedMethods(List.of("GET", "OPTIONS"));
        source.registerCorsConfiguration("/public/**", publicConfig);
        
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_API_ENDPOINTS).permitAll()
                        .requestMatchers(PUBLIC_RESOURCES).permitAll()
                        .requestMatchers(API_PREFIX + "/**").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
