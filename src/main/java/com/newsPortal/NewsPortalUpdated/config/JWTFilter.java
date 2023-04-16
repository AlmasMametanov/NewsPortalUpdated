package com.newsPortal.NewsPortalUpdated.config;

import com.newsPortal.NewsPortalUpdated.security.JWTUtil;
import com.newsPortal.NewsPortalUpdated.services.impl.AppUserDetailsService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, AppUserDetailsService appUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && !authHeader.isBlank()) {
            String jwt = authHeader.substring(7);
            if (!jwt.isBlank()) {
                try {
                    String email = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JwtException exception) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Invalid JWT Token\"}");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Invalid JWT Token in Bearer Header\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
