package myboard.user;

import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest(classes = MyBoardApplication.class)
public class MailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendSimpleMessageTest() throws MessagingException {
        // given
        String toMail = "lccss7@naver.com";
        String confirmMail = "124124WQRQWR1254!@#sometestconfirmlink";

        // when
        emailService.sendFindUserConfirmMail(toMail, confirmMail);

        // then
        // 오류 안나면 성공, 메일 왔는지 직접 확인
    }
}
