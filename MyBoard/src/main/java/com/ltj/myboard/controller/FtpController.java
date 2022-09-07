package com.ltj.myboard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FtpController {

    @PostMapping("/userfile/post/{postID}")
    public String uploadUserImage(@RequestParam MultipartFile upload){
        return "";
    }
}
