package com.springboot.student_management_system.config;

import com.springboot.student_management_system.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    //security configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->
                        auth

                                //Get-> USER + ADMIN
                                .requestMatchers("/auth/login")
                                .permitAll()

                                .requestMatchers(HttpMethod.GET,
                                        "/api/students/**")
                                .hasAnyRole("USER","ADMIN")

                                .requestMatchers(HttpMethod.POST,
                                        "/api/students/**")
                                .hasRole("ADMIN")

                                .requestMatchers(HttpMethod.PUT,
                                        "/api/students/**")
                                .hasRole("ADMIN")

                                .requestMatchers(HttpMethod.PATCH,
                                        "/api/students/**")
                                .hasRole("ADMIN")

                                .requestMatchers(HttpMethod.DELETE,
                                        "/api/students/**")
                                .hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authenticationProvider(
                        authenticationProvider()
                )
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(
                userDetailsService
        );
        provider.setPasswordEncoder(
                passwordEncoder
        );
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception{
        return config.getAuthenticationManager();
    }



}
