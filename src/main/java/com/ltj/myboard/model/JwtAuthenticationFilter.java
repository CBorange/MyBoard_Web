package com.ltj.myboard.model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> attrs = Collections.list(request.getAttributeNames());
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 유효한 토큰인지 확인
        if(token != null && !token.isEmpty() && token.startsWith("Bearer ")){
            String jwtToken = token.substring(7);
            if(jwtTokenProvider.validateToken(jwtToken)){
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
                Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
                // SecurityContext 에 Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.debug("JWT Token does not begin with Bearer String");
            }
        }

        filterChain.doFilter(request, response);
    }
}
