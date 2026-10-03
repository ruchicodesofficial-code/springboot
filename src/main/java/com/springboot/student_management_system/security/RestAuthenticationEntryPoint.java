package com.springboot.student_management_system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        System.out.println(">>> AUTHENTICATION ENTRY POINT CALLED");
        System.out.println(">>> REQUEST URI: " + request.getRequestURI());
        System.out.println(">>> METHOD: " + request.getMethod());
        System.out.println(">>> AUTH EXCEPTION: " + authException.getMessage());
        System.out.println(">>> AUTHENTICATION: "
                + SecurityContextHolder.getContext().getAuthentication());

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        objectMapper.writeValue(
                response.getOutputStream(),
                Map.of(
                        "status", 401,
                        "message", "Authentication required"
                )
        );
    }
}