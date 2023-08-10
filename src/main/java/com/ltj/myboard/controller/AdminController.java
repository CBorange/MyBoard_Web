package com.ltj.myboard.controller;

import com.ltj.myboard.domain.User;
import com.ltj.myboard.dto.admin.AdminChangePassword;
import com.ltj.myboard.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @PatchMapping("/admin/change-password")
    public ResponseEntity changePassword(@RequestBody AdminChangePassword request){
        User user = adminService.changePassword(request);
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
