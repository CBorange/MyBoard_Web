package com.ltj.myboard.service;

import com.ltj.myboard.domain.User;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@AllArgsConstructor
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if(principal == null) return;
        if(!(principal instanceof UserDetailsImpl)) return;

        UserDetailsImpl userDetails = (UserDetailsImpl)principal;
        User user = userDetails.getUser();

        user.setLoginDay(new Date());
        userRepository.save(user);

        response.sendRedirect("/");
    }
}
