package com.pain.yellow.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.pain.yellow.app.filter.RestAuthenticationFilter;
import com.pain.yellow.app.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final PasswordService passwordService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.mvcMatchers("/**"))
                .authorizeRequests(req -> req
                        .mvcMatchers("/ping").permitAll()
                        .anyRequest().authenticated())
                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .userDetailsPasswordManager(passwordService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String defaultId = "bcrypt";
        Map<String, PasswordEncoder> map = new HashMap<>();
        map.put(defaultId, new BCryptPasswordEncoder());
        map.put("pbkdf2", new Pbkdf2PasswordEncoder());
        return new DelegatingPasswordEncoder(defaultId, map);
    }

    private RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestAuthenticationFilter restAuthenticationFilter = new RestAuthenticationFilter(objectMapper);
        restAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        restAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        restAuthenticationFilter.setAuthenticationManager(authenticationManager());
        restAuthenticationFilter.setFilterProcessesUrl("/authorize/login");
        return restAuthenticationFilter;
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (req, res, auth) -> {
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println(objectMapper.writeValueAsString(auth));
        };
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (req, res, ex) -> {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            Map<String, String> map = ImmutableMap.of(
                            "title", "authentication failed",
                            "detail", ex.getMessage());
            res.getWriter().println(objectMapper.writeValueAsString(map));
        };
    }
}
