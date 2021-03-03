package com.pain.yellow.security.util;

import com.pain.yellow.security.config.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    public static final Key accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final AuthProperties authProperties;

    public String genAccessToken(UserDetails userDetails) {
        return genJwtToken(userDetails, authProperties.getJwt().getAccessTokenExpireTime(), accessKey);
    }

    public String genRefreshToken(UserDetails userDetails) {
        return genJwtToken(userDetails, authProperties.getJwt().getRefreshTokenExpireTime(), refreshKey);
    }

    public String genAccessTokenWithRefreshToken(String refreshToken) {
        return parseClaims(refreshToken, refreshKey).map(claims -> Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + authProperties.getJwt().getAccessTokenExpireTime()))
                .signWith(accessKey, SignatureAlgorithm.HS512).compact())
                .orElseThrow(() -> new BadCredentialsException("refresh invalid"));
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessKey, true);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey, false);
    }

    public Optional<Claims> parseClaims(String jwtToken, Key signKey) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean validateToken (String token, Key signKey, boolean ignoreExpire) {
        try {
            Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            if (ignoreExpire && e instanceof ExpiredJwtException) {
                return true;
            }

            return false;
        }
    }

    private String genJwtToken(UserDetails userDetails, long timeToExpire, Key key) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setId("yellow")
                .claim("authorities", userDetails
                        .getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now)).setExpiration(new Date(now + timeToExpire))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
