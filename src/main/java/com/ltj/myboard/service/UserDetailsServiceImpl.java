package com.ltj.myboard.service;

import com.ltj.myboard.domain.User;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
/**
 * formLogin 기본 인증Filter 인증 처리용 UserDetailsService 구현체*/
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = userRepository.findById(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("등록되지 않은 사용자 입니다.");
                });
        return new UserDetailsImpl(found);
    }
}
