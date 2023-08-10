package com.ltj.myboard.service;

import com.ltj.myboard.domain.User;
import com.ltj.myboard.dto.admin.AdminChangePassword;
import com.ltj.myboard.repository.UserGradeRepository;
import com.ltj.myboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserGradeRepository userGradeRepository;
    private final PasswordEncoder passwordEncoder;

    public User changePassword(AdminChangePassword request){
        User found = userRepository.findById(request.getUserId()).orElseThrow(
            () ->{
                throw new NoSuchElementException("어드민 Control : 유저 비밀번호 변경 실패, 유저 없음");
            }
        );

        found.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(found);

        return found;
    }
}
