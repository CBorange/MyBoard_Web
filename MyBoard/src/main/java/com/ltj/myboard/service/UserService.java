package com.ltj.myboard.service;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Optional<User> findUserByID(String ID)
    {
        return userRepository.findUserByID(ID);
    }

    public List<User> findUserByGrade(int grade)
    {
        return userRepository.findUserByGrade(grade);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> found = userRepository.findUserByID(username);
        if(found.isEmpty())
            throw new UsernameNotFoundException("등록되지 않은 사용자 입니다.");
        return found.get();
    }
}
