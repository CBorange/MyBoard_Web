package com.ltj.myboard.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController extends LayoutControllerBase {

    @GetMapping("/login")
    public String loginPage(Model model) {
        addLayoutModel_FragmentContent(model,"login.html", "login");
        return LayoutViewPath;
    }
}
