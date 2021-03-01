package com.pain.yellow.security.util;

import com.pain.yellow.security.config.AuthProperties;
import com.pain.yellow.security.pojo.Role;
import com.pain.yellow.security.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        jwtUtil = new JwtUtil(new AuthProperties());
    }

    @Test
    void genJwtToken() {
        String username = "user";
        Set<Role> authorities = new HashSet<>();

        authorities.add(Role.builder().authority("ROLE_USER").build());
        authorities.add(Role.builder().authority("ROLE_ADMIN").build());

        User user = User.builder().username(username).authorities(authorities).build();

        String token = jwtUtil.genAccessToken(user);

        Claims body = Jwts.parserBuilder()
                .setSigningKey(JwtUtil.accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, body.getSubject(), "subject 不匹配");
    }
}