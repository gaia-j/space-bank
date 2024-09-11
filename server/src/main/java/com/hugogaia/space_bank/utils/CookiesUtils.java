package com.hugogaia.space_bank.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;


import java.util.Arrays;

@Component
public class CookiesUtils {

    public String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public void setTokenCookie(String token, HttpServletResponse response) {
        String domain = "localhost";
        if(System.getenv("prod") != null) {
            domain = ".onrender.com";
        }
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setDomain(domain);
        response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=none; Secure");
        response.addCookie(cookie);
    }
}
