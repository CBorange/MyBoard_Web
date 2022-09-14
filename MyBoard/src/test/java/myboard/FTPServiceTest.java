package myboard;

import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.service.FtpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MyBoardApplication.class)
public class FTPServiceTest {
    @Autowired
    private FtpService ftpService;

    @Test
    public void FTPServiceTest(){
    }
}
