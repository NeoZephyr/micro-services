package com.pain.yellow.security.filter;

import com.pain.yellow.security.util.CollectionUtil;
import com.pain.yellow.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static String jwtHeader = "Authorization";
    private static String jwtHeaderPrefix = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (validJwtToken(request)) {
            Optional<Claims> optionalClaims = extractJwtToken(request)
                    .filter(claims -> claims.get("authorities") != null);

            if (optionalClaims.isPresent()) {
                Claims claims = optionalClaims.get();
                System.out.println("claims: " + claims);
                List<?> claimList =  CollectionUtil.convertObjectToList(claims.get("authorities"));
                List<SimpleGrantedAuthority> authorities = claimList
                        .stream()
                        .map(String::valueOf)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private Optional<Claims> extractJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(jwtHeader).replace(jwtHeaderPrefix, "");

        try {
            return Optional.of(Jwts
                    .parserBuilder()
                    .setSigningKey(JwtUtil.accessKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private boolean validJwtToken(HttpServletRequest request) {
        String header = request.getHeader(jwtHeader);
        return StringUtils.isNotBlank(header) && header.startsWith(jwtHeaderPrefix);
    }
}
