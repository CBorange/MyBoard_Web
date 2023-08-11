package com.ltj.myboard.service;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.domain.UserGrade;
import com.ltj.myboard.dto.auth.ChangeUserInfoRequest;
import com.ltj.myboard.dto.auth.ChangeUserPasswordRequest;
import com.ltj.myboard.dto.auth.RegistUserRequest;
import com.ltj.myboard.dto.auth.TokenResponseDTO;
import com.ltj.myboard.model.JwtTokenProvider;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.model.UserGradeLevel;
import com.ltj.myboard.repository.UserGradeRepository;
import com.ltj.myboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserGradeRepository userGradeRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 토큰 생성
     * @deprecated JWT Token 인증 사용안함
     */
    @Deprecated
    public TokenResponseDTO generateAccessToken(ChangeUserInfoRequest req){
        // AuthenticationManager를 실행하면 ProviderManager에서 등록된 AuthenticationProvider 중
        // 인증처리가 가능한 AuthenticationProvider로 인증을 진행하고 Authentication 객체를 반환해준다.
        // SpringSecurity는 기본적으로 DaoAuthenticationProvider가 구현되어 있고
        // 따라서 아래 구문을 실행하면 자동으로 UserDetialsService를 호출하여 DB에서 로그인 검증하여
        // 반환해준다.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUserID(), req.getCurPassword())
        );

        return new TokenResponseDTO(createAccessToken(authentication), jwtTokenProvider.getTokenExpirationTime());
    }

    private String createAccessToken(Authentication authentication){
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);
        return token;
    }

    /**
     * 유효한 사용자 인지 검사한다.<br/>
     * DB에 조회해보고 Password가 일치하는지 대조한다
     * @return 유효한 로그인 이라면 유저정보 반환, 아니면 null 반환
     * */
    public User validateUser(String userId, String userPassword){
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isEmpty())
            return null;

        User user = foundUser.get();
        if(passwordEncoder.matches(userPassword, user.getPassword()))
            return user;
        else
            return null;
    }

    public User registerUser(RegistUserRequest request) {
        validateRegistUserParamWithException(request);

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setNickname(request.getNickname());
        newUser.setId(request.getUserID());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRegisterDay(new Date());

        // 회원가입 시 기본 등급은 유저
        UserGrade defaultGrade = userGradeRepository.findByGrade(UserGradeLevel.User.getValue()).orElseThrow(
            () -> {
                String msg = String.format("registUser 오류발생, 유저 등급 값[%d]에 해당하는 유저 등급 데이터가 존재하지 않음",
                        UserGradeLevel.User.getValue());
                throw new NoSuchElementException(msg);
            });
        newUser.setUserGrade(defaultGrade);
        userRepository.save(newUser);

        return newUser;
    }

    private void validateRegistUserParamWithException(RegistUserRequest request){
        // 아이디 중복검사
        long cnt = userRepository.countById(request.getUserID());
        if(cnt > 0)
            throw new IllegalArgumentException("이미 사용중인 아이디 입니다.");
        // 이메일 중복검사
        cnt = userRepository.countByEmail(request.getEmail());
        if(cnt > 0)
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
        // 닉네임 중복검사
        cnt = userRepository.countByNickname(request.getNickname());
        if(cnt > 0)
            throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
    }

    private boolean validateUserIsSame(String requestUserId){
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails)context .getAuthentication().getPrincipal();
        String curUserId = userDetails.getUsername();
        if(!curUserId.equals(requestUserId)){
            return false;
        }
        return true;
    }

    private boolean validateEmailExists(String requestUserId, String requestEmail){
        long cnt = userRepository.countByEmailAndIdNot(requestEmail, requestUserId);
        // 변경하려는 이메일을 사용하고 있으면서 현재 Request를 시도한 유저와 같지 않은 아이디를 가진 케이스의 개수
        if(cnt > 0)
            return false;
        return true;
    }

    private boolean validateNicknameExists(String requestUserId, String requestNickname){
        long cnt = userRepository.countByNicknameAndIdNot(requestNickname, requestUserId);
        // 변경하려는 닉네임을 사용하고 있으면서 현재 Request를 시도한 유저와 같지 않은 아이디를 가진 케이스의 개수
        if(cnt > 0)
            return false;
        return true;
    }

    public User changeUserInfo(ChangeUserInfoRequest request) {
        // 현재 로그인된 유저와 아이디 비교하여 다를경우 Exception 발생
        if(!validateUserIsSame(request.getUserID()))
            throw new IllegalArgumentException("유저정보가 같지 않습니다.");

        User existUser = validateUser(request.getUserID(), request.getCurPassword());
        if(existUser == null){
            // 로깅용 msg
            String logMsg = String.format("유저 ID[%s], 비밀번호[%s]에 해당하는 유저를 찾을 수 없음, 유저정보 변경 실패",
                    request.getUserID(), request.getCurPassword());
            log.info(logMsg);

            // Exception Throw 용 msg
            String exceptionMsg = String.format("[%s]의 비밀번호가 틀렸습니다.", request.getUserID());
            throw new NoSuchElementException(exceptionMsg);
        }

        // 이미 다른 유저가 사용중인 이메일 인지
        if(!validateEmailExists(request.getUserID(), request.getEmail()))
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");

        if(!validateNicknameExists(request.getUserID(), request.getNickname()))
            throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");

        existUser.setNickname(request.getNickname());
        existUser.setEmail(request.getEmail());

        userRepository.save(existUser);
        return existUser;
    }

    public String changeUserPassword(ChangeUserPasswordRequest request){
        // 현재 로그인된 유저와 아이디 비교하여 다를경우 Exception 발생
        if(!validateUserIsSame(request.getUserId()))
            throw new IllegalArgumentException("유저정보가 같지 않습니다.");

        User existUser = validateUser(request.getUserId(), request.getCurPassword());
        if(existUser == null){
            // 로깅용 msg
            String logMsg = String.format("유저 ID[%s], 비밀번호[%s]에 해당하는 유저를 찾을 수 없음, 비밀번호 변경 실패",
                    request.getUserId(), request.getCurPassword());
            log.info(logMsg);

            // Exception Throw 용 msg
            String exceptionMsg = String.format("[%s]의 비밀번호가 틀렸습니다.", request.getUserId());
            throw new NoSuchElementException(exceptionMsg);
        }

        existUser.setPassword(passwordEncoder.encode(request.getAfterPassword()));

        userRepository.save(existUser);
        return existUser.getPassword();
    }
}
