package com.ltj.myboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    // URL Route 시, Servlet Dispatcher 동작 전 Filter 단계에서 처리할 Spring Security Filter Bean 등록 및 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            // TODO CORS/CSRF 걸려서 POST 날리면 403 에러뜸 수정 필요
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/writepostform/**").hasAnyRole() // 글쓰기, 편집은 권한이 있어야만 가능
            .anyRequest().permitAll();
        // Login 화면 설정
        http.formLogin((form) -> form
                    .loginPage("/login")
                    .permitAll()
                )
                .logout((logout) -> logout.permitAll());
            // TODO Filter 설정 for JWT
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
