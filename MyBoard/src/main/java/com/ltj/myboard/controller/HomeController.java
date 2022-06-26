package com.ltj.myboard.controller;
import ch.qos.logback.core.Layout;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController extends LayoutControllerBase {
    @GetMapping("/")
    public String home(Model model){
        addLayoutModel_FragmentContent(model,"hello.html", "hello");

        return LayoutViewPath;
    }
}
