package com.newsPortal.NewsPortalUpdated.config;

import com.newsPortal.NewsPortalUpdated.security.CustomAccessDeniedHandler;
import com.newsPortal.NewsPortalUpdated.security.CustomAuthenticationEntryPoint;
import com.newsPortal.NewsPortalUpdated.services.impl.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AppUserDetailsService appUserDetailsService;
    private final JWTFilter jwtFilter;


    @Autowired
    public SecurityConfig(AppUserDetailsService appUserDetailsService, JWTFilter jwtFilter) {
        this.appUserDetailsService = appUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/article").hasRole("AUTHOR")
                .antMatchers(HttpMethod.PUT, "/article").hasRole("AUTHOR")
                .antMatchers(HttpMethod.DELETE, "/articles/{articleId}").hasAnyRole("ADMIN", "AUTHOR")
                .antMatchers(HttpMethod.POST, "/category").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/category").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/categories/{categoryId}").hasRole("ADMIN")
                .antMatchers("/users", "/rolesManagement/**", "/user/role").hasRole("ADMIN")
                .antMatchers("/users/{email}", "/user/**").authenticated()
                .antMatchers("/auth/login", "/auth/registration", "/**").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}
