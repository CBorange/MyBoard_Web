package myboard.user;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.FindUserRequest;
import com.ltj.myboard.repository.redis.FindUserRequestRepository;
import com.ltj.myboard.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = MyBoardApplication.class)
@Transactional
public class FindUserRepositoryTest {

    @Autowired
    private FindUserRequestRepository findUserRequestRepository;

    @Autowired
    private UserService  userService;

    @Test
    public void addPendingStateForFindUserTest(){
        // given
        String email = "test@example.com";

        // when
        FindUserRequest ret = userService.makeFindUserRequestAndSetToPending(email);

        // then
        FindUserRequest found = findUserRequestRepository.findByUniqueLink(ret.getUniqueLink());
        Assertions.assertNotNull(found);
    }

}
