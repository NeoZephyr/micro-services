package com.pain.yellow.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(100)
public class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(req -> req.anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .rememberMe(remember -> remember.key("remember-me").tokenValiditySeconds(60 * 3).rememberMeCookieName("remember-me"))
                .logout(logout -> logout.logoutUrl("/perform_logout").logoutSuccessUrl("/login"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
