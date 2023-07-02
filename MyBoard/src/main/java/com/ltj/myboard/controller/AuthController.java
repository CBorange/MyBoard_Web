package com.ltj.myboard.controller;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.dto.auth.AuthDTO;
import com.ltj.myboard.dto.auth.TokenResponseDTO;
import com.ltj.myboard.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController extends LayoutControllerBase {
    @Value("${server.domain}")
    private String serverDomain;

    private final AuthService authService;

    // 로그인 페이지 반환, login post api는 spring security 자체적으로 제공한다.
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        // 로그인 페이지 이동 전 마지막 페이지 caching
/*        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);*/
        
        // 로그인 페이지 반환
        addLayoutModel_FragmentContent(model,"login.html", "login");
        return LayoutViewPath;
    }

    // 로그인 기능 실행, Session-Base의 경우 기본으로 구현된 FormLogin용 login Post API를 사용한다.
    // Token-Base일 경우에만 이 API 구현해서 사용
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(HttpServletResponse response, @RequestBody AuthDTO loginReq){
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

    // 비밀번호 변경 페이지 반환
    @GetMapping("/changepassword")
    public String changePasswordPage(Model model){
        addLayoutModel_FragmentContent(model, "changePassword.html", "changePassword");
        return LayoutViewPath;
    }

    // 회원가입 기능 실행
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity register(@RequestBody AuthDTO request){
        try
        {
            User newUser = authService.registerUser(request);

            return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 비밀번호 변경 기능 실행
    @PostMapping("/changepassword")
    public ResponseEntity changePassword(@RequestBody AuthDTO request){
        try
        {
            if(request.getPassword().equals(request.getAfterPassword())){
                String msg = "현재 비밀번호와 변경 후 비밀번호가 동일합니다.";
                log.info(msg);

                return ResponseEntity.badRequest().body(msg);
            }
            User changedUser = authService.changePassword(request);
            return new ResponseEntity<User>(changedUser, HttpStatus.OK);
        }catch (IllegalStateException e){
            String msg = "아이디 또는 비밀번호가 일치하지 않습니다.";
            log.info(msg);
            return ResponseEntity.badRequest().body(msg);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // test
    @PostMapping("/apitest")
    public ResponseEntity sslTest(){
        return ResponseEntity.ok().body("success");
    }
}
