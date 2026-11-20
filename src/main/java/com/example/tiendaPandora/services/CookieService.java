package com.example.tiendaPandora.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

@Service
public class CookieService {

    public String getCookie(HttpServletRequest request, String nombreCookie) {
        Cookie cookie = WebUtils.getCookie(request, nombreCookie);
        return cookie != null ? cookie.getValue() : null;
    }

    public void addHttpOnlyCookie(String name, String value, int maxAge, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteCookie(String name, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}