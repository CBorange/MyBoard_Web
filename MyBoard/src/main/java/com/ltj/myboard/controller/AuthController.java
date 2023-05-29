package com.ltj.myboard.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController extends LayoutControllerBase {

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
    @GetMapping("/changePassword")
    public String changePasswordPage(Model model){
        addLayoutModel_FragmentContent(model, "changePassword.html", "changePassword");
        return LayoutViewPath;
    }

    public ResponseEntity register(){

    }
}
