package com.ltj.myboard;
import com.ltj.myboard.service.MyAuthenticationFailureHandler;
import com.ltj.myboard.service.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

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
            .antMatchers("/writepostform/**").authenticated()
            .antMatchers("/mypage/**").authenticated()
            .antMatchers("/changepassword").authenticated()
            .anyRequest().permitAll();  // 그 외 나머지 API는 권한 없어도 접근 가능
        // Login 화면 설정
        http.formLogin((form) -> form
                    .loginPage("/login")
                    .successHandler(myAuthenticationSuccessHandler)
                    .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        // Remember-me
        http.rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(3600)
                .alwaysRemember(true)
                .userDetailsService(userDetailsService);

        // SessionManagement
        http.sessionManagement()
                .invalidSessionUrl("/invalid")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/expired");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
