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


package com.management.management_crm.config;

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
                        .requestMatchers("/customers/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/restaurant/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/restaurant/**").permitAll()
                        .requestMatchers("/auth/**", "/auth.html", "/email-sent-success.html", "/dashboard.html",
                                "/password-reset-success.html", "/reset-password.html", "/auth/register", "/auth/login", 
                                "/dashboard", "/auth/forgot-password", "/users", "/customers", "/restaurant", "/auth/reset-password", "/css/**",
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
}