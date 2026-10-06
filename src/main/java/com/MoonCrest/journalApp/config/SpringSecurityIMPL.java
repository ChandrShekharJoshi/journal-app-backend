package com.MoonCrest.journalApp.config;

import com.MoonCrest.journalApp.security.CustomAccessDeniedHandler;
import com.MoonCrest.journalApp.security.CustomAuthenticationEntryPoint;
import com.MoonCrest.journalApp.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityIMPL {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAuthenticationEntryPoint
            customAuthenticationEntryPoint;

    private final CustomAccessDeniedHandler
            customAccessDeniedHandler;


    public SpringSecurityIMPL(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler) {

        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

        this.customAuthenticationEntryPoint =
                customAuthenticationEntryPoint;

        this.customAccessDeniedHandler =
                customAccessDeniedHandler;
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                customUserDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**")
                        .permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                // 401 and 403 handling
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                customAuthenticationEntryPoint
                        )

                        .accessDeniedHandler(
                                customAccessDeniedHandler
                        )
                )

                .authenticationProvider(
                        authenticationProvider()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}