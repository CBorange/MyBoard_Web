package com.ltj.myboard.controller;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.dto.auth.AuthDTO;
import com.ltj.myboard.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController extends LayoutControllerBase {
    private final AuthService authService;

    // 로그인 페이지 반환, login post api는 spring security 자체적으로 제공한다.
    @GetMapping("/login")
    public String loginPage(Model model) {
        addLayoutModel_FragmentContent(model,"login.html", "login");
        return LayoutViewPath;
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
}