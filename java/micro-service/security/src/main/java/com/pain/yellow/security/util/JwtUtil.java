package com.pain.yellow.security.util;

import com.pain.yellow.security.config.AuthProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
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

    public String genJwtToken(UserDetails userDetails, long timeToExpire, Key key) {
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
