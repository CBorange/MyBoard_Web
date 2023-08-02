package myboard;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = MyBoardApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void 유저획득_ByID(){
        User findUser = userService.findUserByID("admin");
    }
}
