package com.cafe.cafe_management.JWT;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTUtils jwtUtils;
    private CustomUserDetailsService service;

    DecodedJWT claims = null;
    private String username = null;

    @Autowired
    public JWTFilter(JWTUtils jwtUtils, CustomUserDetailsService service){
        this.jwtUtils = jwtUtils;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")){
            filterChain.doFilter(request, response);
        }else{
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;
            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);
                username = jwtUtils.extractUsername(token);
                claims = jwtUtils.extractAllClaims(token);
            }
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = service.loadUserByUsername(username);
                if(jwtUtils.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public boolean isAdmin(){
        if(claims!=null) {
            return "admin".equalsIgnoreCase(claims.getClaim("role").asString());
        }else{
            return false;
        }
    }

    public boolean isUser(){
        if(claims!=null){
            return "user".equalsIgnoreCase((String) claims.getPayload());
        }else{
            return false;
        }
    }

    public String getCurrentUser(){
        return username;
    }
}
