package com.ltj.myboard.service;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.repository.UserNotificationRepository;
import com.ltj.myboard.repository.UserRepository;
import com.ltj.myboard.util.Ref;
import com.ltj.myboard.util.UserNotiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserNotificationRepository userNotificationRepository;

    public User findUserByID(String ID)
    {
        User user = userRepository.findById(ID)
                .orElseThrow(() -> new NoSuchElementException("cannot found userInformation by " + ID));
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = userRepository.findById(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("등록되지 않은 사용자 입니다.");
                });
        return new UserDetailsImpl(found);
    }
}
