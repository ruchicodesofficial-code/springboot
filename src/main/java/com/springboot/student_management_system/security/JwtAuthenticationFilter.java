package com.springboot.student_management_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader==null||!authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        final String jwtToken = authorizationHeader.substring(7);
        final String username;
        try{
            username= jwtService.extractUsername(jwtToken);

        }catch (Exception e){
            System.out.println("JWT validation failed: " + e.getMessage());
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (username!=null &&
                SecurityContextHolder.getContext()
                        .getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("Validating JWT for: " + username);
            if (jwtService.isTokenValid(
                    jwtToken,userDetails
            )){
                UsernamePasswordAuthenticationToken authentication = new
                        UsernamePasswordAuthenticationToken(
                                userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
                System.out.println("Authentication set successfully");
            }
            else {
            System.out.println("JWT token is invalid.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
            }
        }
        filterChain.doFilter(request,response);
    }
}
