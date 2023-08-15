package com.ltj.myboard;
import com.ltj.myboard.model.JwtTokenProvider;
import com.ltj.myboard.model.UserGradeLevel;
import com.ltj.myboard.service.MyAuthenticationFailureHandler;
import com.ltj.myboard.service.MyAuthenticationSuccessHandler;
import com.ltj.myboard.service.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    // URL Route 시, DispatcherServlet 동작 전 Filter 단계에서 처리할 Spring Security Filter Bean 등록 및 설정
    // Spring Security는 기본적으로 Cookie, Session을 사용하여 유저정보를 저장한다.
    // 기본적인 formLogin 사용 시 로그인 성공하면 cookie를 생성하고(set-cookie header 반환)
    // 세션관리자에서 유저 세션을 생성한다(cookie 정보로)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
/*        // URI별로 보안설정
        http.authorizeRequests()
                // 주의, authenticated는 인증만 되어있으면 허용한다는 의미임
                // hasAnyAuthorithy, hasAnyRoles 얘네는 지정된 특정 권한이 있어야 접근할 수 있다는 의미
            .antMatchers("/writepostform/**").authenticated()
            .antMatchers(HttpMethod.POST, "/ftp/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/ftp/**").authenticated()
            .antMatchers("/comment").authenticated()
            .antMatchers("/mypage/**").authenticated()
            .antMatchers("/changepassword").authenticated()
            .antMatchers("/apitest").authenticated()
            .anyRequest().permitAll() // 그 외 나머지 API는 권한 없어도 접근 가능

            // Jwt인증필터를 기본 UsernamePasswordAuthenticationFilter 전에 넣는다.
            // UsernamePasswordAuthenticationFilter는 해당 Filter가 실행될 시점에 SecurityContext가 등록돼있으면
            // 인증처리를 실행하지 않는다. 따라서 Jwt필터가 먼저 실행돼서 SecuirtyContext에 인증정보 넣어줘야 함
            .and().addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // JWT(Token-Base) 인증방식
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/

        // URI별로 보안설정
        http.authorizeRequests()
        // 주의, authenticated는 인증만 되어있으면 허용한다는 의미임
        // hasAnyAuthorithy, hasAnyRoles 얘네는 지정된 특정 권한이 있어야 접근할 수 있다는 의미
            .antMatchers("/writepostform/**").authenticated()
            .antMatchers(HttpMethod.POST, "/ftp/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/ftp/**").authenticated()
            .antMatchers("/comment").authenticated()
            .antMatchers("/mypage/**").authenticated()
            .antMatchers("/changeuserinfo").authenticated()
                .antMatchers("/changeuserpassword").authenticated()
            .antMatchers("/apitest").authenticated()
            .antMatchers(HttpMethod.PATCH,"/user").authenticated()
            .antMatchers(HttpMethod.PUT,"/user/password").authenticated()
            .antMatchers("/user/notification/**").authenticated()
            .antMatchers("/admin/**").hasAuthority("ROLE_" + UserGradeLevel.Admin.getValue())
            .anyRequest().permitAll(); // 그 외 나머지 API는 권한 없어도 접근 가능

        // Login 화면 설정
        http.formLogin((form) -> form
                    .loginPage("/login")
                    .successHandler(myAuthenticationSuccessHandler)
                    .failureHandler(myAuthenticationFailureHandler)
                    .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(myLogoutSuccessHandler)
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        // Remember-me
        http.rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(3600)
                .alwaysRemember(false)
                .userDetailsService(userDetailsService);

        // SessionManagement
        http.sessionManagement()
                //.invalidSessionUrl("/invalid")
                .maximumSessions(1)// 최대 세션 수
                .maxSessionsPreventsLogin(true); // 다중 로그인 허용여부(max Session 넘어서 로그인 하면 튕겨내는지)
                //.expiredUrl("/expired");

        // test
        //http.csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
