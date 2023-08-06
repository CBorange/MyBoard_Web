package myboard;

import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.dto.auth.ChangeUserInfoRequest;
import com.ltj.myboard.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MyBoardApplication.class)
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    public void 유저정보변경_비밀번호제외(){
    }

    public void 유저정보변경_비밀번호포함(){

    }
}
