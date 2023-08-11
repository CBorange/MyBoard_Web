package myboard.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.dto.auth.ChangeUserPasswordRequest;
import com.ltj.myboard.dto.auth.RegistUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(classes = MyBoardApplication.class)
@AutoConfigureMockMvc
@Transactional
public class RegistUerTest {
    @Autowired
    private MockMvc mvc;

    // DTO Json 변환을 위한 ObjectMapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 회원가입 API(유효성검사) 실패 케이스 테스트
    @Test
    public void registUser_WrongDTO() throws Exception{
        // given(유효하지 않은 DTO 작성)
        RegistUserRequest request = new RegistUserRequest();
        request.setUserID("user1");
        request.setNickname("!@$!@$!@");
        request.setPassword("123123");
        request.setEmail("test!@#");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 회원가입 API(존재하는 아이디) 실패 케이스 테스트
    @Test
    public void registUser_ExistId() throws Exception {
        // given
        RegistUserRequest request = new RegistUserRequest();
        request.setUserID("admin");
        request.setNickname("유저1");
        request.setPassword("pass1234!@");
        request.setEmail("test12@example.com");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 회원가입 API(존재하는 이메일) 실패 케이스 테스트
    @Test
    public void registUser_ExistEmail() throws Exception {
        // given
        RegistUserRequest request = new RegistUserRequest();
        request.setUserID("user1");
        request.setNickname("유저1");
        request.setPassword("pass1234!@");
        request.setEmail("test@example.com");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    // 회원가입 API(존재하는 닉네임) 실패 케이스 테스트
    @Test
    public void registUser_ExistNickname() throws Exception {
        // given
        RegistUserRequest request = new RegistUserRequest();
        request.setUserID("user1");
        request.setNickname("관리자");
        request.setPassword("pass1234!@");
        request.setEmail("test12@example.com");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 회원가입 API 성공 케이스 테스트
    @Test
    public void registUser_Success() throws Exception {
        // given
        RegistUserRequest request = new RegistUserRequest();
        request.setUserID("user1");
        request.setNickname("유저1");
        request.setPassword("pass1234!@");
        request.setEmail("test12@example.com");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
