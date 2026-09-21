package com.example.tiendaPandora.security.jwt;

import java.io.IOException;

import com.example.tiendaPandora.services.CookieService;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tiendaPandora.services.TokenBlackListService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenBlackListService serviceBlackList;
    private final CookieService cookieService;

    public JwtFilter(JwtService jwtService,
                     CustomUserDetailsService userDetailsService,
                     TokenBlackListService serviceBlackList, CookieService cookieService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.serviceBlackList = serviceBlackList;
        this.cookieService = cookieService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String username = null;
            String accessToken = cookieService.getCookie(request, "accessToken");

            if(accessToken != null) {
                username = jwtService.extractUsername(accessToken);
            }

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtService.isTokenValid(accessToken, userDetails) && !serviceBlackList.esTokenInvalido(accessToken)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            response.setStatus(401);
        }

    }

}
