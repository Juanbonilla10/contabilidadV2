/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author L E N O V O
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        String bearerToken = request.getHeader("Authorization");
        
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            String token = bearerToken.replace("Bearer ", "");
            UsernamePasswordAuthenticationToken usernamePat = TokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePat);
        }
        
        filterChain.doFilter(request, response);
                
    }
    
}
