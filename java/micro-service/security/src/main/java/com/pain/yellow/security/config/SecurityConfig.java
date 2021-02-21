package com.pain.yellow.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pain.yellow.security.filter.RestAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final SecurityProblemSupport securityProblemSupport;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(req -> req.anyRequest().authenticated())
//                .formLogin(form -> form.loginPage("/login")
//                        .defaultSuccessUrl("/")
//                        .successHandler(authenticationSuccessHandler())
//                        .failureHandler(authenticationFailureHandler())
//                        .permitAll())
//                .httpBasic(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable())
//                .rememberMe(remember -> remember.tokenValiditySeconds(60 * 3).rememberMeCookieName("remember-me"))
//                .logout(logout -> logout.logoutUrl("/perform_logout"));

        http
                .exceptionHandling(exp -> exp
                        .authenticationEntryPoint(securityProblemSupport)
                        .accessDeniedHandler(securityProblemSupport))
                .authorizeRequests(req -> req
                .antMatchers("/authorize/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.ignoringAntMatchers("/authorize/**", "/admin/**", "/users/**"));
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
        String defaultId = "bcrypt";
        Map<String, PasswordEncoder> map = new HashMap<>();
        map.put(defaultId, new BCryptPasswordEncoder());
        map.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
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
