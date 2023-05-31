package com.ltj.myboard.model;

import com.ltj.myboard.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";  // Spring Security의 Role 기본 접두어는 ROLE_ 이다
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        int grade = user.getUserGrade().getGrade();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + String.valueOf(grade));
        Collection<GrantedAuthority> authorities = new ArrayList<>();   // 사용자가 여러개의 권한을 가질 수 있으므로 List로 처리
        authorities.add(authority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
