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

    @Test
    public void parseMailTemplate(){
        // given
        String toMail = "lccss7@naver.com";
        String confirmMail = "124124WQRQWR1254!@#sometestconfirmlink";

        // when
        String html = emailService.parseMailTemplate(toMail, confirmMail);

        // then
        System.out.println("======================Parsed MailTemplate==============================");
        System.out.println(html);
        System.out.println("=======================================================================");
        // 직접 html 확인해서 문제 없는지 추가적으로 확인
    }
}
