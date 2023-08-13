package myboard.user;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.FindUserPending;
import com.ltj.myboard.repository.FindUserPendingRepository;
import com.ltj.myboard.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = MyBoardApplication.class)
public class FindUserRepositoryTest {

    @Autowired
    private FindUserPendingRepository  findUserPendingRepository;

    @Autowired
    private AuthService authService;

    @Test
    public void addPendingStateForFindUserTest(){
        // given
        String email = "test@example.com";

        // when
        FindUserPending ret = authService.addFindUserStateToPending(email);

        // then
        FindUserPending found = findUserPendingRepository.findByUniqueLinkParam(ret.getUniqueLinkParam());
        Assertions.assertNotNull(found);
    }
}
