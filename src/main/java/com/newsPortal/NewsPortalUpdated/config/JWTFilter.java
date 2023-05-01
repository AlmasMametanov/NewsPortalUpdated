package com.newsPortal.NewsPortalUpdated.config;

import com.newsPortal.NewsPortalUpdated.security.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && !authHeader.isBlank()) {
            String jwt = authHeader.substring(7);
            if (!jwt.isBlank()) {
                try {
                    Claims claims = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    claims.get("roles", Collection.class).forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            claims.get("email", String.class), null, authorities);
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JwtException exception) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Invalid JWT Token\"}");
                    logger.info("Invalid JWT Token - " + exception);
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Invalid JWT Token in Bearer Header\"}");
                logger.info("Invalid JWT Token in Bearer Header - " + logger.getName());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
