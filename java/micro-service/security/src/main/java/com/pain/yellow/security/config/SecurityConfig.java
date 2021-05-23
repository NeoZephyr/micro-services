package com.pain.yellow.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pain.yellow.security.filter.JwtFilter;
import com.pain.yellow.security.filter.RestAuthenticationFilter;
import com.pain.yellow.security.service.impl.PasswordService;
import com.pain.yellow.security.service.impl.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Configuration
@Order(99)
@Import(SecurityProblemSupport.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final ObjectMapper objectMapper;
    private final SecurityProblemSupport securityProblemSupport;
    private final DataSource dataSource;
    private final UserService userService;
    private final PasswordService passwordService;
    private final JwtFilter jwtFilter;
//    private final RoleHierarchyService roleHierarchyService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.mvcMatchers("/users/**", "/authorize/**"))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exp -> exp
                        .authenticationEntryPoint(securityProblemSupport)
                        .accessDeniedHandler(securityProblemSupport))
                .authorizeRequests(req -> req
                .antMatchers("/authorize/**").permitAll()
                .antMatchers("/users/token").permitAll()
                .antMatchers("/users/register").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                // .antMatchers("/api/user/{username}").access("hasRole('ADMIN') or authentication.name.equals('#username')")
                // .antMatchers("/api/user/{username}").access("hasRole('ADMIN') or @userService.isValidUser(authentication, #username)")
                .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 配置跨域
                .formLogin(form -> form.disable())
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout.disable());

        // TODO disable basic
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .mvcMatchers("/resources/**", "/static/**", "/public/**", "/h2-console/**", "/error/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许跨域访问的主机
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4001"));
        } else {
            configuration.setAllowedOrigins(Collections.singletonList("https://uaa.imooc.com"));
        }
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.addExposedHeader("X-Authenticate");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @ConditionalOnProperty(prefix = "auth.security", name = "role-hierarchy-enabled", havingValue = "true")
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy(roleHierarchyService.getRoleHierarchyExpr());
        return roleHierarchy;
    }
}
