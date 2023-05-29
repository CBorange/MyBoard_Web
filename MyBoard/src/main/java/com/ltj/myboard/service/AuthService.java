package com.ltj.myboard.service;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;

    public Optional<User> registUser()
    {

    }
}
