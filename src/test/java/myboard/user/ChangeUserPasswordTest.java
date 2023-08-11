package myboard.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.dto.auth.ChangeUserPasswordRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(classes = MyBoardApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ChangeUserPasswordTest {
    @Autowired
    private MockMvc mvc;

    // DTO Json 변환을 위한 ObjectMapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 유저비밀번호 변경 API(유효성검사) 실패 케이스 테스트
    @Test
    public void changePassword_InvalidDTO() throws Exception{
        // given(유효하지 않은 DTO 작성)
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest();
        request.setUserId("admin");
        request.setCurPassword("437624");
        // 유효하지 않은 비밀번호 형식
        // 8자 이상, 영문,숫자,기호 사용가능, 영문 대문자 또는 특수기호 1개이상 포함필수
        request.setAfterPassword("123");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .put("/user/password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())   // CSRF 토큰
                        .with(user("admin")))   // 인증이 필요한 API를 호출하기 위해 principal 만들어주는 mock util 실행
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void changePassword_NotSameUser() throws Exception{
        // given(유효하지 않은 DTO 작성)
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest();
        request.setUserId("admin");
        request.setCurPassword("437624");
        request.setAfterPassword("examplePW123!");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .put("/user/password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())   // CSRF 토큰
                        .with(user("ltj2020")))   // Request와 다른 유저 로그인
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void changePassword_Success() throws Exception{
        // given
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest();
        request.setUserId("admin");
        request.setCurPassword("437624");
        request.setAfterPassword("examplePW123!");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .put("/user/password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())   // CSRF 토큰
                        .with(user("admin")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
