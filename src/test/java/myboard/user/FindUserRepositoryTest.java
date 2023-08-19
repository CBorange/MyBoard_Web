package myboard.user;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.dto.auth.FindUserResult;
import com.ltj.myboard.repository.redis.FindUserRequestRepository;
import com.ltj.myboard.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@SpringBootTest(classes = MyBoardApplication.class)
/**@Transactional
/ Redis의 Transaction은 RDB의 Transaction과는 다르게
/ MULTI-EXEC 커맨드의 조합으로 실행된다.
/ 일반적인 RDB의 Transaction은 Isolation Level 설정에 따라
/ 실행중인 Transaction의 변경사항을 다른 Transaction에서 Select하거나 하지 못하게 할 수 있다.
/ 하지만 Redis의 Transaction은 MULTI-EXEC 커맨드를 통해 Transaction하게 처리되어야 할 커맨드를 커맨드 Queue에 대기
/ 시켜두었다가 Transaction으로 묶어서 실행하는 기능이 정상적으로 Return 되면 그 때 메시지 Queue의 커맨드를 실행한다.
/ 따라서 Transaction 실행중에 key 값으로 데이터를 조회해도 같은 Transaction 내라고 해서 데이터가 조회되지 않는다
/ (아직 커맨드가 실행된게 아니기 때문에) 따라서 개별 실행에 대해서 하나의 처리를 보장한다는 '원자성'은 구현되지만
/ 테스트중에 값이 제대로 저장되었는지(즉 커맨드 실행이 정상적으로 이루어졌는지에 대한 검증)은 할 수 없다.
/ 그래서 그냥 수동으로 Del 명령어 호출하도록 테스트 코드에서 실행하게끔 하도록 함, 따라서 테스트코드에서 Transactional 사용안함*/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FindUserRepositoryTest {
    @Autowired
    private FindUserRequestRepository findUserRequestRepository;

    @Autowired
    private UserService  userService;

    private String userPassword;
    private String uniqueLink;

    @Test
    public void addFindUserState_UnitTest(){
        // given
        String email = "test@example.com";

        // when
        String uniqueLink = userService.makeFindUserRequestAndSetToPending(email);

        // then
        String userId = findUserRequestRepository.findByUniqueLink(uniqueLink);

        // Rollback
        findUserRequestRepository.remove(uniqueLink);
    }

    @Test
    @Order(1)
    public void addFindUserState_Integration(){
        // given
        String email = "ltjbs2020@gmail.com";

        // when
        uniqueLink = userService.makeFindUserRequestAndSetToPending(email);

        // then
        String userId = findUserRequestRepository.findByUniqueLink(uniqueLink);
    }

    @Test
    @Transactional  // JPA 실행부분에 대해서는 롤백되어야 함
    @Order(2)
    public void changePasswordTemporally_Integration(){
        // given
        String email = "ltjbs2020@gmail.com";
        String beforePassword = userService.findUserByEmail(email).getPassword();   // 임시비밀번호로 변경 전 비밀번호

        // when
        FindUserResult ret = userService.changeUserPasswordTemporallyByFindUserRequest(uniqueLink);

        // then
        // 임시비밀번호로 바꾸면서 Redis Key 사라져야됨
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            String userId = findUserRequestRepository.findByUniqueLink(uniqueLink);
        });
    }
}
