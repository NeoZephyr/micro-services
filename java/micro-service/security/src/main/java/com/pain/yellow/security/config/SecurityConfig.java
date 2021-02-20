package com.pain.yellow.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(req -> req.anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .rememberMe(remember -> remember.tokenValiditySeconds(60 * 3).rememberMeCookieName("remember-me"))
                .logout(logout -> logout.logoutUrl("/perform_logout"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring().mvcMatchers("/public/**", "/error")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("123456"))
                .roles("USER", "ADMIN");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (req, res, auth) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println(objectMapper.writeValueAsString(auth));
        };
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (req, res, exp) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            Map<String, String> map = new HashMap<>();
            map.put("title", "authentication failed");
            map.put("detail", exp.getMessage());
            res.getWriter().println(objectMapper.writeValueAsString(map));
        };
    }
}
