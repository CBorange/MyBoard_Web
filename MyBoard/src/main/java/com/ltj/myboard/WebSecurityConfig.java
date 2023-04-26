package com.ltj.myboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    // URL Route 시, Servlet Dispatcher 동작 전 Filter 단계에서 처리할 Spring Security Filter Bean 등록 및 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/writepostform").hasAnyRole() // 글쓰기, 편집은 권한이 있어야만 가능
                .and()
                // 토큰기반 인증이므로 Session 사용하지 않음
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // TODO Filter 설정
        return http.build();
    }
}
