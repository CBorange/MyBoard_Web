package com.ltj.myboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    // URL Route 시, Servlet Dispatcher 동작 전 Filter 단계에서 처리할 Spring Security Filter Bean 등록 및 설정
    // Spring Security는 기본적으로 Cookie, Session을 사용하여 유저정보를 저장한다.
    // 기본적인 formLogin 사용 시 로그인 성공하면 cookie를 생성하고(set-cookie header 반환)
    // 세션관리자에서 유저 세션을 생성한다(cookie 정보로)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // TODO CORS/CSRF 걸려서 POST 날리면 403 에러뜸 수정 필요
        http.csrf().disable();
        // URI별로 보안설정
        http.authorizeRequests()
                // 주의, authenticated는 인증만 되어있으면 허용한다는 의미임
                // hasAnyAuthorithy, hasAnyRoles 얘네는 지정된 특정 권한이 있어야 접근할 수 있다는 의미
            .antMatchers("/writepostform/**").authenticated() // 글쓰기, 편집은 권한이 있어야만 가능
            .anyRequest().permitAll();  // 그 외 나머지 API는 권한 없어도 접근 가능
        // Login 화면 설정
        http.formLogin((form) -> form
                    .loginPage("/login")
                    .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .permitAll());
            // TODO Filter 설정 for JWT
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

/*    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }*/
}
