package com.ltj.myboard.service;
import com.ltj.myboard.model.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Component
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 시도한 username, password parameter
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String msg = String.format("login 실패 [username: %s], [password: %s] 오류내용 : %s", username, password, exception.getMessage());
        log.info(msg);

        response.sendRedirect("/login?error");
    }
}
