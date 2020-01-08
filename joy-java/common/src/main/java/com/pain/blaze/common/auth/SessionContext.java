package com.pain.blaze.common.auth;

import com.pain.blaze.common.crypto.Sign;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class SessionContext {

    public static final long SHORT_SESSION = TimeUnit.HOURS.toMillis(12);
    public static final long LONG_SESSION = TimeUnit.HOURS.toMillis(30 * 24);

    public static void login(String userId,
                             boolean support,
                             boolean rememberMe,
                             String signToken,
                             String externalApex,
                             HttpServletResponse response) {
        long duration = SHORT_SESSION;

        if (rememberMe) {
            duration = LONG_SESSION;
        }

        int maxAge = (int)(duration / 1000);

        String token = Sign.genSessionToken(userId, support, signToken, duration);
        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setDomain(externalApex);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void logout(String externalApex, HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setDomain(externalApex);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        Cookie candCookie = null;

        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), AuthConstant.COOKIE_NAME)) {
                candCookie = cookie;
                break;
            }
        }

        if (candCookie != null) {
            return candCookie.getValue();
        } else {
            return null;
        }
    }
}
