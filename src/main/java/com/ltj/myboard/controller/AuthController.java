package com.ltj.myboard.controller;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.dto.auth.ChangeUserInfoRequest;
import com.ltj.myboard.dto.auth.RegistUserRequest;
import com.ltj.myboard.dto.auth.TokenResponseDTO;
import com.ltj.myboard.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController extends LayoutControllerBase {
    @Value("${server.domain}")
    private String serverDomain;

    private final AuthService authService;
    private final RestTemplate restTemplate;

    // 로그인 페이지 반환, login post api는 spring security form login 자체적으로 제공한다.
    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {
        // 로그인 페이지 이동 전 마지막 페이지 caching
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);
        
        // 로그인 페이지 반환
        addLayoutModel_FragmentContent(model,"login.html", "login");
        return LayoutViewPath;
    }

    // 로그인 기능 실행, Session-Base의 경우 기본으로 구현된 FormLogin용 login Post API를 사용한다.
    // Token-Base일 경우에만 이 API 구현해서 사용
    @Deprecated(since = "token 기반 인증 사용안함")
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(HttpServletResponse response, @RequestBody ChangeUserInfoRequest loginReq){
        try{
            // 쿠키에 Jwt 토큰 담아서 반환
            TokenResponseDTO accessToken = authService.generateAccessToken(loginReq);
            Cookie cookie = new Cookie("accessToken", accessToken.getAccessToken());
            cookie.setDomain(serverDomain);
            cookie.setPath("/");
            cookie.setMaxAge((int)(accessToken.getAccessTokenExpirationTime() * 60));
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return new ResponseEntity<TokenResponseDTO>(accessToken, HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            String msg = e.getMessage();
            log.info(msg);
            return ResponseEntity.badRequest().body(msg);
        }
        catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 회원가입 페이지 반환
    @GetMapping("/register")
    public String registerPage(Model model){
        addLayoutModel_FragmentContent(model, "register.html", "register");
        return LayoutViewPath;
    }

    // 유저정보 수정 페이지 반환
    @GetMapping("/changeuserinfo")
    public String changeUserInfoPage(Model model){
        addLayoutModel_FragmentContent(model, "changeUserInfo.html", "changeUserinfo");
        return LayoutViewPath;
    }

    // 비밀번호 변경 페이지 반환
    @GetMapping("/changeuserpassword")
    public String changeUserPasswordPage(Model model){
        addLayoutModel_FragmentContent(model, "changeUserPassword.html", "changeUserPassword");
        return LayoutViewPath;
    }
}
