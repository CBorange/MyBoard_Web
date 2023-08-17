package myboard.user;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.repository.redis.FindUserRequestRepository;
import com.ltj.myboard.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addPendingStateForFindUser(){
        // given
        String email = "test@example.com";

        // when
        String uniqueLink = userService.makeFindUserRequestAndSetToPending(email);

        // then
        String userId = findUserRequestRepository.findByUniqueLink(uniqueLink);
    }
}
