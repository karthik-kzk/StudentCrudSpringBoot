package com.example.demo.config;

import com.example.demo.payload.ErrorResponse;
import com.example.demo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter (HttpServletRequest request) throws ServletException{
        AntPathMatcher pathMatcher =new AntPathMatcher();
        List<String> excludeUrlPatterns=new ArrayList();
        excludeUrlPatterns.add("/student/authenticate");
       return excludeUrlPatterns.stream().anyMatch(v->pathMatcher.match(v,request.getServletPath()));


    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwtToken = null;
        String username = null;
        String bearerToken = request.getHeader("Authorization");





        try {

            if (shouldNotFilter(request)==false) {
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

                jwtToken = bearerToken.substring(7, bearerToken.length());
                username = jwtUtil.extractUsername(jwtToken);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        else{
            throw new Exception("Authorization header is invalid/not found");
        }
        }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            response.setContentType("application/json");
            response.setStatus(400);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            System.out.println(e);
        }


    }
}