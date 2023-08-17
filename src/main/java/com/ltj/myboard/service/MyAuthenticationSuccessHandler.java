package com.ltj.myboard.service;

import com.ltj.myboard.domain.User;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.repository.jpa.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@AllArgsConstructor
@Component
@Slf4j
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

        // 리다이렉트
        HttpSession session = request.getSession(false);
        if(session != null){
            String redirectURL = (String) session.getAttribute("prevPage");
            if(redirectURL != null){
                String[] splitedURL = redirectURL.split("/");
                String lastPath = splitedURL[splitedURL.length - 1];

                // 마지막 페이지가 게시판 혹은 게시글이 아닌경우 홈 화면으로 리다이렉트
                if(!(lastPath.contains("/board") || lastPath.contains("/post"))){
                    redirectURL = "/";
                }

                session.removeAttribute("prevPage");
                response.sendRedirect(redirectURL);
                log.info("login success, redirect to " + redirectURL);
            }
        } else{
            log.info("login success, redirect to " + "/");
            response.sendRedirect("/");
        }
    }
}
