package com.ltj.myboard.service;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.domain.UserGrade;
import com.ltj.myboard.dto.auth.AuthDTO;
import com.ltj.myboard.model.UserGradeLevel;
import com.ltj.myboard.repository.UserGradeRepository;
import com.ltj.myboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

import java.util.Date;
import java.util.Optional;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserGradeRepository userGradeRepository;
    private final PasswordEncoder passwordEncoder;

    // 유효한 사용자 인지 검사한다(DB에 등록된 사용자가 맞는지 로그인 해봄)
    public Optional<User> validateUser(String userId, String userPassword){
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isEmpty())
            return foundUser;   // null

        User user = foundUser.get();
        if(passwordEncoder.matches(userPassword, user.getPassword()))
            return foundUser;
        else
            return Optional.empty();
    }

    public User registerUser(AuthDTO request) {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setNickname(request.getNickname());
        newUser.setId(request.getUserID());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRegisterDay(new Date());

        // 회원가입 시 기본 등급은 유저
        UserGrade defaultGrade = userGradeRepository.findByGrade(UserGradeLevel.User.getValue()).orElseThrow();
        newUser.setUserGrade(defaultGrade);
        userRepository.save(newUser);

        return newUser;
    }

    public User changePassword(AuthDTO request) {
        User existUser = validateUser(request.getUserID(), request.getPassword())
                .orElseThrow(() -> new IllegalStateException(""));
        existUser.setPassword(passwordEncoder.encode(request.getAfterPassword()));

        userRepository.save(existUser);
        return existUser;
    }
}
