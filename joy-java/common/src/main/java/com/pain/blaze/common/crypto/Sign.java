package com.pain.blaze.common.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pain.blaze.common.error.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Sign {

    public static final String EMAIL_CLAIM = "email";
    public static final String USER_ID_CLAIM = "userId";
    public static final String SUPPORT_CLAIM = "support";

    private static Map<String, Algorithm> algorithmMap = new HashMap<>();
    private static Map<String, JWTVerifier> jwtVerifierMap = new HashMap<>();

    private static Algorithm getAlgorithm(String signToken) {
        Algorithm algorithm = algorithmMap.get(signToken);

        if (algorithm == null) {
            synchronized (Sign.class) {
                algorithm = algorithmMap.get(signToken);

                if (algorithm == null) {
                    algorithm = Algorithm.HMAC512(signToken);
                    algorithmMap.put(signToken, algorithm);
                }
            }
        }

        return algorithm;
    }

    public static String genEmailConfirmToken(String userId, String email, String signToken) {

        if (StringUtils.isBlank(signToken)) {
            throw new ServiceException("No signing token present");
        }

        Algorithm algorithm = algorithmMap.get(signToken);
        String token = JWT.create()
                .withClaim(EMAIL_CLAIM, email)
                .withClaim(USER_ID_CLAIM, userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2)))
                .sign(algorithm);
        return token;
    }

    public static String genSessionToken(String userId, boolean support, String signToken, long duration) {

        if (StringUtils.isBlank(signToken)) {
            throw new ServiceException("No signing token present");
        }

        Algorithm algorithm = algorithmMap.get(signToken);
        String token = JWT.create()
                .withClaim(USER_ID_CLAIM, userId)
                .withClaim(SUPPORT_CLAIM, support)
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(algorithm);
        return token;
    }

    public static DecodedJWT verifyToken(String token, String signToken) {
        JWTVerifier jwtVerifier = jwtVerifierMap.get(signToken);

        if (jwtVerifier == null) {
            synchronized (Sign.class) {
                jwtVerifier = jwtVerifierMap.get(signToken);

                if (jwtVerifier == null) {
                    Algorithm algorithm = Algorithm.HMAC512(signToken);
                    jwtVerifier = JWT.require(algorithm).build();
                    jwtVerifierMap.put(signToken, jwtVerifier);
                }
            }
        }

        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }
}
