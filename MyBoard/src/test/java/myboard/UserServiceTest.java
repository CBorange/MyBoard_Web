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
        Optional<User> findUser = userService.findUserByID("admin");
        Assertions.assertTrue(findUser.isPresent());
    }

    @Test
    public void 유저획득_ByGrade(){
        List<User> findUser = userService.findUserByGrade(1);
        Assertions.assertTrue(findUser.stream().count() > 0);
    }
}
