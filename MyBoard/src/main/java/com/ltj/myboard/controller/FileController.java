package com.ltj.myboard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class FileController {

    @PostMapping("/image")
    public String uploadUserImage(@RequestPart(value = "image", required = false) MultipartFile image){
        return "";
    }
}
