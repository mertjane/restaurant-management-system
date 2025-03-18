package com.management.management_crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and() // Enable CORS
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/register", "/auth/forgot-password", "/auth/reset-password", "/templates/**").permitAll()
                        .requestMatchers("/frontend/**").permitAll() // Allow frontend files
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                .csrf().disable() // Disable CSRF for simplicity (enable in production with proper config)
                .formLogin()
                    .loginPage("http://localhost:5500/frontend/auth.html") // Set custom login page URL
                    .permitAll()
                    .defaultSuccessUrl("http://localhost:5500/frontend/dashboard.html", true)
                .and()
                .httpBasic().disable(); // Disable basic auth for testing
        return http.build();
    }
}