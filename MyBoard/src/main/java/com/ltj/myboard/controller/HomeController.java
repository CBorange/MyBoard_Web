package com.ltj.myboard.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends LayoutControllerBase {
    @GetMapping("/")
    public String home(Model model){
        addLayoutModel_FragmentContent(model,"hello.html", "hello");

        return LayoutViewPath;
    }
}
